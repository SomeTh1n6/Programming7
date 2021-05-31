package com.server_command;

import com.utility.Request;
import com.utility.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadRequest {
    private final Serializer serializer;

    public ReadRequest(){
        serializer = new Serializer();
    }
    public Request readRequest(SelectionKey selectionKey) throws IOException, ClassNotFoundException {
        byte[] b = new byte[65536];
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer f = ByteBuffer.wrap(b);
        f.clear();
        int numRead = 1;
        while (numRead > 0) {
            numRead = socketChannel.read(f);
        }
        Request request = (Request) serializer.deserialize(b);
        request.setSocketChannel(socketChannel);
        return request;

    }
}
