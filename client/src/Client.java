import com.commands.Add;
import com.commands.History;
import com.commands.IsNumber;
import com.utility.*;
import com.worker.Worker;

import javax.jws.soap.SOAPBinding;
import java.io.*;

import java.net.*;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Client {
    private int port;
    private String host;
    private Socket socket;
    private Response response;
    private String userCommand;
    private byte[] buffer;
    private Request clientRequest;
    private Serializer serializer;
    private Queue<String> history;
    private short COMMAND_HISTORY_SIZE = 6;
    private boolean running = false;


    public Client(String host, int port){
        this.port = port;
        this.host = host;
        serializer = new Serializer();
        history = new ArrayDeque<>();
    }

    /**
     * Запуск клиентского приложения, обмен сообщениями с сервером
     * */
    public void run() throws IOException, ClassNotFoundException {
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Ожидание подключения...");
            socket = new Socket(host,port);
            System.out.println("Подключение успешно");
            UserData userInformation = auth();
            while (!socket.isClosed()){             // Пока соединение с сервером живет
                buffer = new byte[65556];
                if (scanner.hasNextLine()){
                    userCommand = scanner.nextLine();
                    clientRequest = parseRequest(userCommand,userInformation);
                    System.out.println(clientRequest.toString());// парсим строку клиента
                    if (clientRequest.getCommand().equals("exit")){
                        socket.close();
                        System.out.println("Вы успешно отключились от сервера");
                    }
                    else{
                        if (clientRequest.getCommand().equals("add")){          // исполняем подготовку к отравке клиентом объекта класса Worker
                            Worker worker = new Add().execute();
                            String s = clientRequest.getCommand();
                            clientRequest = new Request(s,worker,userInformation);
                            //System.out.println(clientRequest.toString());
                        }
                        else if(clientRequest.getCommand().equals("update")){     // аналогичное add
                            if (!new IsNumber().isNumberInteger(clientRequest.getArgument())) {
                                System.out.println("Введите корректные значение id");
                                continue;
                            }
                            Worker worker = new Add().execute();
                            clientRequest = new Request(clientRequest.getCommand(),clientRequest.getArgument(),worker,userInformation);
                        }
                        if ((clientRequest.getCommand().equals("remove_by_id")))
                        {
                            if (!new IsNumber().isNumberInteger(clientRequest.getArgument())) {
                                System.out.println("Введите корректные значение id");
                                continue;
                            }
                        }
                        if(clientRequest.getCommand().equals("execute_script")){        // записываем в отдельную строку всю информацию о скрипте
                            try{
                                String line;
                                File file = new File(clientRequest.getArgument());
                                FileReader fileReader = new FileReader(file);
                                BufferedReader bufferedReader = new BufferedReader(fileReader);  // считываем из файла
                                StringBuilder sb = new StringBuilder();
                                while ((line = bufferedReader.readLine()) != null) {
                                    sb.append(line).append("\n");
                                }
                                String script = sb.toString();
                                String command = clientRequest.getCommand();
                                String argument = clientRequest.getArgument();
                                for (String s : script.split("\n")) {       // записываем в историю команд команды из файла с скриптом
                                    if (checkCommand(s)){
                                        history.add(s);
                                        if (history.size() > COMMAND_HISTORY_SIZE)
                                            history.remove();
                                    }
                                }
                                clientRequest = new Request(command,argument,script,userInformation);
                            }catch (FileNotFoundException | NullPointerException e){
                                System.out.println("Ошибка при чтении файла");
                            }
                        }
                        sendRequest(clientRequest);             // отправка
                        response = getResponse();               // принятие
                        System.out.println("Ответ от сервера " + response.getResponce());

                        if (clientRequest.getCommand().equals("history")){
                            System.out.println(new History().execute(history));
                        }
                        if (checkCommand(clientRequest.getCommand())){
                            history.add(clientRequest.getCommand());
                            if (history.size() > COMMAND_HISTORY_SIZE)
                                history.remove();
                        }
                    }
                }else{
                    System.out.println("Неожиданная ошибка");
                    System.exit(1);
                }

            }
        }catch (IOException e){
            //e.printStackTrace();
            System.out.println("Сервер недоступен...");
        }

    }

    /**
     * @param clientRequest - запрос, который надо отправить
     * @throws IOException - идея ругается, типо если сокет закрыт
     */
    private void sendRequest(Request clientRequest) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(serializer.serializeRequest(clientRequest));
    }

    /**
     * Получаем ответ от сервера классом Response
     * @return объект класа Response
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Response getResponse() throws IOException, ClassNotFoundException {
        //InputStream inputStream = socket.getInputStream();
        InputStream inputStream = socket.getInputStream();
        inputStream.read(buffer);                                              // Считываем в буфер байты ответа от сервера
        response = serializer.deserializeResponse(buffer);
        return  response;
        //return new Response("");
    }


    /**
     * Парсим введенную строку. создаем экземпляр класса Request
     * @param userCommand строка , которую нам надо спарсить
     * @return экземпляр класса Request
     */
    private Request parseRequest(String userCommand,UserData userData) {
        String[] finalUserCommand = userCommand.split(" ", 2);
        Request request;
        if (finalUserCommand.length == 2) {                                     // Формируем объкт класса Request
            request = new Request(finalUserCommand[0], finalUserCommand[1], userData);
        } else if (finalUserCommand.length == 1) {
            request = new Request(finalUserCommand[0],userData);
        } else {
            request = new Request("", userData);
        }
        return request;
    }


    /**
     * Проверяем , является ли отправленная команда клиента - командой на самом деле
     * @param userCommand - комнда клиента
     * @return - true|false в зависимости от результата
     */
    private boolean checkCommand(String userCommand){
        String[] commands = {"help", "info","show","add","update","remove_by_id id","exit","head","remove_head","history","average_of_salary"
                ,"filter_starts_with_name","print_ascending"};
        for (String c : commands) {             // Непосредственная проверка
            if (userCommand.equals(c)){
                return true;
            }
        }
        return false;
    }

    private UserData auth() throws IOException, ClassNotFoundException {
        response = new Response("");
        UserData ud = null;
        while (!response.getResponce().equals("Вы успешно зарегистрировались!")
                && !response.getResponce().equals("Вы успешно вошли")){
            buffer = new byte[4096];
            System.out.println("Для дальнейшей работы необходима авторизация");
            ud = new Authorization().authorization();
            sendRequest(new Request("authorization",ud));
            response = getResponse();
            System.out.println(response.getResponce());
        }

        return ud;
    }
}