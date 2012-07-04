package com.interview.web.http;

import java.io.IOException;
import java.io.PrintStream;

public class HttpGenericMethodHandler extends HttpFileHandler implements HttpHandler {

    public static final int HTTP_BAD_METHOD = 405;

    @Override
    public void handle(String method, HttpRequest request, PrintStream out) throws IOException {
        send405(out);
    }

    /**
     * Send bad method error.
     *
     * @param out
     * @throws java.io.IOException
     */
    private void send405(PrintStream out) throws IOException {
        sendError(out, HTTP_BAD_METHOD, "Method not supported.");
    }
}
