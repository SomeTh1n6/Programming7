package com.utility;

import com.CollectionManager;
import com.CommandHandler;
import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class RequestHandlerRunnable implements Runnable{
    private SocketChannel socketChannel;
    private Request request;
    private Response response;
    private CollectionManager manager;
    private Serializer serializer;
    private final ExecutorService sendResponse = Executors.newFixedThreadPool(10);
    private final ExecutorService requestHandler = Executors.newFixedThreadPool(10);

    public RequestHandlerRunnable(SocketChannel socketChannel, CollectionManager manager){
        this.socketChannel = socketChannel;
        this.manager = manager;
        serializer = new Serializer();
    }


    @Override
    public void run() {
        try{
            // Получение запроса
            Thread t = new Thread(()->{
                byte[] b = new byte[65536];
                ByteBuffer f = ByteBuffer.wrap(b);
                f.clear();
                try {
                    socketChannel.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    request = (Request) serializer.deserialize(b);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            t.start();

            // Обработка запроса
            requestHandler.submit(() -> {
                try {
                    if (request != null)
                        response = new Response(new CommandHandler().run(manager,request));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

            // Отправка ответа на клиент
            sendResponse.submit(()->{
                ByteBuffer f1 = null;
                try {
                    f1 = ByteBuffer.wrap(serializer.serialize(response));
                    socketChannel.write(f1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception ignored){}
    }
}
