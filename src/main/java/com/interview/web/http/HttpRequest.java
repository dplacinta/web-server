package com.interview.web.http;

import com.interview.web.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Map<String, String> headers;
    private Map<String, String> parameters;

    public HttpRequest() {
        this.headers = new HashMap<String, String>();
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setParameter(String name, String value) {
        if(parameters == null) {
            parameters = new HashMap<String, String>();
        }

        parameters.put(name, value);
    }

    public String getHeader(String headerName) {
        if(headers.containsKey(headerName)) {
            return headers.get(headerName);
        }

        return Constants.EMPTY_STRING;
    }

    public String getParameter(String paramName) {
        if(parameters.containsKey(paramName)) {
            return parameters.get(paramName);
        }

        return Constants.EMPTY_STRING;
    }
}
