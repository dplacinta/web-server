package com.interview.web.http;

import java.io.IOException;
import java.io.PrintStream;

public interface HttpHandler {
    void handle(String method, HttpRequest request, PrintStream out) throws IOException;

    public String getRoot();

    public void setRoot(String root);

    public HttpHandler getSuccessor();

    public void setSuccessor(HttpHandler successor);
}
