package com.interview.web.http;

import com.interview.web.utils.Constants;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URLDecoder;

public class HttpPostHandler extends HttpFileHandler implements HttpHandler {

    @Override
    public void handle(String method, HttpRequest request, PrintStream out) throws IOException {
        if (method.equalsIgnoreCase(POST)) {
            doPost(request);
            // setting the method to GET for chaining
            method = GET;
        }

        getSuccessor().handle(method, request, out);
    }

    /**
     * Upon a post request if the file/folder doesn't exist, it will be created,
     * if the content parameter is present in the request, it will be considered the content for the newly created file,
     * if it's empty, the intention of creating a directory will be considered
     *
     * @param request
     * @throws IOException
     */
    private void doPost(HttpRequest request) throws IOException {
        String path = request.getHeader(Constants.PATH);
        getLogger().info("POST for " + path);

        String content = request.getParameter(Constants.CONTENT_PARAM_NAME);
        boolean isFile = !StringUtils.isEmpty(content);

        File file = new File(getRoot(), path);
        if (!file.exists()) {
            if (isFile) {
                file.createNewFile();
            } else {
                file.mkdir();
            }
        }

        if (isFile) {
            Writer writer = new FileWriter(file);
            writer.write(URLDecoder.decode(content, "UTF-8"));
            writer.close();
        }
    }
}
