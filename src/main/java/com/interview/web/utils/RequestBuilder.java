package com.interview.web.utils;

import com.interview.web.http.HttpRequest;
import org.apache.commons.lang.StringUtils;

import java.io.*;

public class RequestBuilder {

    public static final int BUFFER_SIZE = 1024;


    public static HttpRequest getRequest(InputStream in) throws IOException {
        HttpRequest request = new HttpRequest();
        String requestContents = readRequestContent(in);
        setRequestContents(request, requestContents);

        return request;
    }

    /**
     * Set request headers and parameters (if any)
     * @param request
     * @param requestContents
     */
    private static void setRequestContents(HttpRequest request, String requestContents) {
        if (!StringUtils.isEmpty(requestContents)) {

            String[] headers = requestContents.split(Constants.NEW_LINE);

            //Parse first header line to get the Method, Path and Protocol
            String[] firstHeader = headers[0].split(Constants.SPACE);
            request.setHeader(Constants.METHOD, firstHeader[0]);
            request.setHeader(Constants.PATH, firstHeader[1]);
            request.setHeader(Constants.PROTOCOL, firstHeader[2]);

            int i;
            //parse remaining headers
            for (i = 1; i < headers.length && !headers[i].equals(Constants.EMPTY_STRING); i++) {
                String[] header = headers[i].split(": ");
                request.setHeader(header[0], header[1]);
            }

            // if there are any parameters, pull them in the request object
            if (++i < headers.length) {
                String[] parameters = headers[i].split("&");
                for (String parameter : parameters) {
                    int index = parameter.indexOf('=');
                    if (index > 0) {
                        request.setParameter(parameter.substring(0, index), parameter.substring(index + 1, parameter.length()));
                    }
                }
            }
        }
    }

    /**
     * Reads request contents from the stream and stores them inputStream a string for further processing
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readRequestContent(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return Constants.EMPTY_STRING;
        }

        StringBuilder requestContents = new StringBuilder();
        InputStream bufferedStream = new BufferedInputStream(inputStream);

        byte[] buffer = new byte[BUFFER_SIZE];

        int offset = 0;
        while (true) {
            int read = bufferedStream.read(buffer, offset, BUFFER_SIZE - offset);
            if (read == -1) {
                break;
            }

            for (int i = offset; i < offset + read; i++) {
                requestContents.append((char) buffer[i]);
            }

            if (read < BUFFER_SIZE) {
                break;
            }
        }

        return requestContents.toString();
    }
}
