package com.interview.web;

import java.nio.channels.SocketChannel;

public class Connection implements Runnable {
    private SocketChannel socketChannel;
    private SocketHandler socketHandler;

    public Connection(SocketChannel socketChannel, SocketHandler socketHandler) {
        this.socketChannel = socketChannel;
        this.socketHandler = socketHandler;
    }

    @Override
    public void run() {
        getSocketHandler().handle(getSocketChannel());
    }

    public SocketHandler getSocketHandler() {
        return socketHandler;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
