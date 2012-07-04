package com.interview.web;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static Logger logger = Logger.getLogger(Server.class);

    private SocketHandler socketHandler;
    private ExecutorService executorService;
    private volatile boolean stopped;
    private ServerSocketChannel serverSocketChannel;
    private int port;

    public Server(SocketHandler socketHandler, int threadsCount, int port) {
        this.socketHandler = socketHandler;
        this.executorService = Executors.newFixedThreadPool(threadsCount);
        this.port = port;
    }

    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            logger.error("Unable to start server", e);
        }
    }

    public void start() throws IOException {
        setServerSocketChannel(ServerSocketChannel.open());

        getServerSocketChannel().socket().bind(new InetSocketAddress(this.getPort()));
        getServerSocketChannel().configureBlocking(false);
        System.out.println("Server started");

        while (!stopped) {
            SocketChannel socketChannel = getServerSocketChannel().accept();

            if (socketChannel != null) {
                getExecutorService().execute(new Connection(socketChannel, getSocketHandler()));
            }
        }
    }

    public void stop() {
        stopped = true;
        try {
            getServerSocketChannel().close();
        } catch (IOException e) {
            logger.error("Error stopping the server", e);
        }
        executorService.shutdown();
        System.out.println("Server shutdown");
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    public SocketHandler getSocketHandler() {
        return socketHandler;
    }

    public int getPort() {
        return port;
    }
}
