import com.*;
import com.server_command.ReadRequest;
import com.server_command.SendResponce;
import com.server_command.UserInputHandler;
import com.utility.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;
import java.util.concurrent.*;

public class Server{
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    private final int port;
    private final String host;
    private SocketChannel socketChannel;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private CollectionManager manager;
    private final Serializer serializer;
    private final ExecutorService readerRequest = Executors.newCachedThreadPool();
    private final ExecutorService requestHandler = Executors.newFixedThreadPool(10);
    private final ExecutorService senderResponce = new ForkJoinPool(10);
    private final Map<SocketChannel, Response> responses = new HashMap<>();
    private final List<OpsChange> pendingChanges = new ArrayList<>();

    public Server(String host,int port, String log) throws IOException {
        this.port = port;
        this.host = host;
        serializer = new Serializer();
        try {
            Handler handler = new FileHandler(log);
            Handler handler1 = new ConsoleHandler();
            logger.setUseParentHandlers(false);
            handler.setFormatter(new LogFormatte());
            logger.addHandler(handler);
            logger.addHandler(handler1);
        }catch (Exception e){
            System.out.println("Путь к файлу, куда писать логи - неверен");
            System.exit(1);
        }
    }

    public void run(){
        try {
            start();
        }catch (IOException e){
           logger.warning(e.getMessage());
        }
        logger.info("Сервер запущен");
        UserInputHandler.getUserInputHandler(manager,logger).start();

        while (true) {
            try{
                for (OpsChange ops : pendingChanges) {
                    if (ops.getOps() == SelectionKey.OP_WRITE) {
                        SelectionKey key = ops.getSocketChannel().keyFor(selector);
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                }
                pendingChanges.clear();

                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isValid()){
                        if (key.isAcceptable()) {
                            socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            logger.info("Новое подключение - успешно!");
                        }
                        if (key.isReadable()){
                            read(key);
                            key.interestOps(0);
                        }
                        if (key.isWritable()){
                            send(key);
                        }
                        iterator.remove();
                    }
                }
            }catch (IOException e){
                try {
                    socketChannel.close();
                } catch (IOException ioException) {
                    logger.warning("Неппредвиденная ошибка :\n"+e.getMessage());
                }
            }
        }
    }

    /**
     * Запуск сервера
     * @throws IOException Ошибки
     */
    private void start() throws IOException {
        try {
            manager = new CollectionManager();
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(host,port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        }catch (IOException | SQLException e){
            System.out.println("Вероятнее всего занят порт");
            logger.warning(e.getMessage());
        }
    }



    /**
     * Класс - форматер для логгера
     * Пишем дату и время запроса - его статус и сообщение
     */
    static class LogFormatte extends Formatter {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd 'и время' hh:mm:ss a zzz");  // текущая дата и время

        @Override
        public String format(LogRecord record){
            return formatForDateNow.format(dateNow) + "\n" + record.getLevel() + ": " + record.getMessage() + "\n\n";
        }
    }

    private Request getRequest(SelectionKey key) throws IOException, ClassNotFoundException {
        Request request = null;
        socketChannel = (SocketChannel) key.channel();
        request = new ReadRequest().readRequest(key);
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_WRITE);
        return request;
    }

    /****
     * Поток для приема запросов от клиентов
     * @param selectionKey чтобы понимать по какому ключу подключился клиент
     */
    private void read(SelectionKey selectionKey) {
        readerRequest.submit(() -> {
            try {
                Request request = getRequest(selectionKey);
                handleRequest(request);
            } catch (IOException | ClassNotFoundException e) {
                try {
                    throw new InterruptedIOException("Клиент отключился");
                } catch (InterruptedIOException interruptedIOException) {
                    logger.info(interruptedIOException.getMessage());
                }
            }
        });
    }

    /***
     * Обработчик запросов. Связан с приемщиком запросов (в нем вызывается этот метод)
     * @param request что обрабатываем
     */
    private void handleRequest(Request request){
        // Обработка запроса
        requestHandler.submit(() -> {
            try {
                Response response;
                synchronized (manager.workers){
                    response = new Response(new CommandHandler().run(manager, request));
                }

                synchronized (responses){
                    responses.put(request.getSocketChannel(),response);
                    synchronized (pendingChanges) {
                        pendingChanges.add(new OpsChange(request.getSocketChannel(), SelectionKey.OP_WRITE));
                    }
                }
                selector.wakeup();
            } catch (FileNotFoundException e) {
                logger.warning(e.getMessage());
            }
        });
    }

    /**
     * Поток для отправки ответа. Не вызывается потоком обработки команд
     * @param selectionKey ключ селектора
     * @throws IOException ошибки
     */
    private void send(SelectionKey selectionKey) throws IOException {
        senderResponce.submit(()->{
            socketChannel = (SocketChannel) selectionKey.channel();
            synchronized (responses){
                logger.info(responses.get(socketChannel).getResponce());
                try {
                    new SendResponce(socketChannel,responses.get(socketChannel)).sendResponse();
                    responses.remove(socketChannel);
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        });
    }

}
