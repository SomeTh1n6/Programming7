package com.utility;

import java.nio.channels.SocketChannel;

public class OpsChange {
    private final SocketChannel socketChannel;
    private final int ops;

    public OpsChange(SocketChannel socketChannel, int ops) {
        this.socketChannel = socketChannel;
        this.ops = ops;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public int getOps() {
        return ops;
    }
}
