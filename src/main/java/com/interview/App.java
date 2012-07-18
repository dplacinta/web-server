package com.interview;

import com.interview.web.*;
import com.interview.web.http.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        String rootFolder = getRootDirectory(args);

        HttpFileHandler httpFileHandler = createHttpHandler(rootFolder);
        Server server = new Server(httpFileHandler, 10, 8888);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(server);

        Thread.sleep(500);
        System.out.println("Hit any key to stop the server");
        System.in.read();

        server.stop();
        service.shutdown();
    }

    private static String getRootDirectory(String[] args) {
        String rootFolder = args.length > 0 ? args[0] : ".";

        System.out.println("Root directory: " + rootFolder);

        return rootFolder;
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
}
