package com.example5;

/**
 * Created by dahyeon on 2015. 9. 4..
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;

public class ServerTest {

    private static final int port = 8888;
    public static boolean flag = false;

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/dh", new MyHandler());       // when connect request, make a new handler
        server.createContext("/dh/weather", new WeatherHandler());
        server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {

        int count = 0;

        @Override
        public void handle(HttpExchange t) throws IOException {

            //new SwingWorker<String, Void>() {};

            String response = "This is the root!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            count += 1;
            System.out.println("root response = " + count);
        }
    }

    static class WeatherHandler implements HttpHandler {

        int count = 0;

        @Override
        public void handle(HttpExchange t) throws IOException {

            //String response = "This is the weather ^^";
            String response = getDataFromOtherServer();
            System.out.println(response);

            //if(flag) {
                byte [] data = response.getBytes(); // 한글의 경우 response.length는 1byte지만 byte[]에서는 2byte임.
                t.sendResponseHeaders(200, data.length);
                OutputStream os = t.getResponseBody();

                os.write(data);
            os.flush();
                os.close();
            //}

            count += 1;
            System.out.println("weather response = " + count);
        }

        public String getDataFromOtherServer(){
            final String url = "http://www.naver.com/";
            String result = "error";

            try{
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                System.out.println("NAVER Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                result = response.toString();

            } catch (Exception e){
                e.printStackTrace();
            }


            return result;
        }
    }
}
