package com.example1;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dahyeon on 2015. 9. 2..
 */
public class CustomServer {

//    private HttpServer httpServer;
//
//    CustomServer(int port) { //}, String resource, HttpHandler handler){
//
//        try {
//            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
//            httpServer.createContext("/dh", new CostomHandler());
//            //httpServer.createContext("/dh/weather", new WeatherHandler());
//            httpServer.setExecutor(Executors.newCachedThreadPool());
//
//        } catch (IOException e) {
//            System.out.println("httpServer create error");
//            e.printStackTrace();
//        }
//    }
//
//    public void start(){
//        this.httpServer.start();
//    }

}
