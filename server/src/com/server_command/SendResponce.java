package com.server_command;

import com.utility.Response;
import com.utility.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendResponce {
    private final SocketChannel socketChannel;
    private final Serializer serializer;
    private final Response response;

    public SendResponce(SocketChannel socketChannel, Response response){
        serializer = new Serializer();
        this.response = response;
        this.socketChannel = socketChannel;
    }

    public void sendResponse() throws IOException {
        ByteBuffer f1 = ByteBuffer.wrap(serializer.serialize(response));
        socketChannel.write(f1);
    }
}
