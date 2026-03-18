package com.init_spring_bean_mvn.demo.httpserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.net.HttpURLConnection.HTTP_OK;

public class SimpleHttpServer {
    private static long visitorCounter = 0;

    // Next will use new Java.net.http package: jdk 11 or greater - Use HttpClient
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0); // system default used, negative number or 0
            // internal package - com.sun.net - jdk 6 class, lightweight httpserver, less performance, good for testing/simple
            // external site would exist and remain static
            // create http context - map ui path to code - exchangeHandler - all requests received for server by path will be given by object for exchange

            server.createContext("/", exchange -> {
                String requestMethod = exchange.getRequestMethod();
                System.out.println("R M:" + requestMethod);

                // Need mrore steps to send client data to http server
                String data = new String(exchange.getRequestBody().readAllBytes());
                System.out.println(" Body data " + data);
                Map<String, String> parameters = parseParameters(data);
                System.out.println(parameters);


                if(requestMethod.equals("POST")) {
                    visitorCounter++;
                }

                String fn = parameters.get("first");
                String ln = parameters.get("last");
                        
                String response = """
                        <html>
                        <body>
                        <h1> Hello from http</h1>
                        <p>Number of Visitors who signed up = %d<p>
                        <form method = "post">
                        <label for="first">First Name: </label>
                        <input type = "text" id="first" name="first" value="%s"/>
                            <br>
                        <label for="last">Last Name: </label>
                        <input type = "text" id="last" name="last" value="%s"/>
                        <br>
                        <input type = "submit" value="Submit"/>
                       </form>
                        </body>
                        </html>
                    """.formatted(visitorCounter,
                        fn == null ? "" : fn,
                        ln == null ? "" : ln);

                // pass byte array required

                try {

                    TimeUnit.SECONDS.sleep(5);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                var bytes = response.getBytes();// need send character type but defaults to system
                exchange.sendResponseHeaders(HTTP_OK, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.close();
                //Response code: 200
                //null
                //null=[HTTP/1.1 200 OK]
                //Content-length=[76]
                //Date=[Thu, 08 Jan 2026 22:24:39 GMT]
                //null
            });
            server.start();
            System.out.println(" Server listening on port 8080");

            // passed to any client serving to web site


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> parseParameters(String requestBody) {
        Map<String, String> parameters = new HashMap<>();
        String[] pairs = requestBody.split("&");
        for(String pair : pairs) {
            String[] kv = pair.split("=");
            if(kv.length == 2) {
                parameters.put(kv[0], kv[1]);
            }
        }
        return parameters;
    }
}
