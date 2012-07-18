package com.interview;

import com.interview.web.*;
import com.interview.web.http.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        String rootFolder = getRootDirectory(args);
        int threadsCount = getThreadsCount(args);
        int port = getPort(args);

        HttpFileHandler httpFileHandler = createHttpHandler(rootFolder);
        Server server = new Server(httpFileHandler, threadsCount, port);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(server);

        Thread.sleep(500);
        System.out.println("Hit any key to stop the server");
        System.in.read();

        server.stop();
        service.shutdown();
    }

    private static HttpFileHandler createHttpHandler(String rootFolder) {
        HttpHandler genericHandler = new HttpGenericMethodHandler();
        genericHandler.setRoot(rootFolder);

        HttpHandler getHandler = new HttpGetHandler();
        getHandler.setRoot(rootFolder);
        getHandler.setSuccessor(genericHandler);

        HttpHandler postHandler = new HttpPostHandler();
        postHandler.setRoot(rootFolder);
        postHandler.setSuccessor(getHandler);

        HttpFileHandler httpFileHandler = new HttpFileHandler();
        httpFileHandler.setRoot(rootFolder);
        httpFileHandler.setSuccessor(postHandler);
        return httpFileHandler;
    }

    private static int getPort(String[] args) {
        return args.length > 2 ? Integer.parseInt(args[2]) : 8888;
    }

    private static int getThreadsCount(String[] args) {
        return args.length > 1 ? Integer.parseInt(args[1]) : 10;
    }

    private static String getRootDirectory(String[] args) {
        String rootFolder = args.length > 0 ? args[0] : ".";

        System.out.println("Root directory: " + rootFolder);

        return rootFolder;
    }
}
