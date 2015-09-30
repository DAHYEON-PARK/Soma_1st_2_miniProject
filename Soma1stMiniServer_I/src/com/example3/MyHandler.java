package com.example3;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by dahyeon on 2015. 9. 4..
 */


public class MyHandler implements HttpHandler {

    private String root = "Users/dahyeon/Downloads/WebServer/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        String requestMethod = exchange.getRequestMethod();
//        if (requestMethod.equalsIgnoreCase("GET")){
//            Headers responseHeaders = exchange.getResponseHeaders();
//            responseHeaders.set("Content-Type", "text/html");
//            URI uri = exchange.getRequestURI();
//            System.out.println(uri.getPath());
//            OutputStream responseBody = exchange.getResponseBody();
//            BufferedReader br = new BufferedReader(new FileReader(root + uri.getPath()));
//            exchange.sendResponseHeaders(200, 0);
//            int b = 0;
//            while((b = br.read()) != -1){
//                responseBody.write(b);
//            }
//            responseBody.close();
//        }
    }
}
