package com.example1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class CustomHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
//
//        URI uri = httpExchange.getRequestURI();
//        response = createResponseFromQueryParams(uri);
    }

}
