package com.init_spring_bean_mvn.demo.networking;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
// this code to phone or browser
public class WebSocketClient {
    public static void main(String[] args) throws URISyntaxException { // added to method signature because whole point of below
        // code is to connect to websocketserver

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name to join chat");
        String name = scanner.nextLine();
        HttpClient client = HttpClient.newHttpClient();
        WebSocket webSocket = client.newWebSocketBuilder().buildAsync(
                new URI("ws://localhost:8080?name=%s".formatted(name)),
                new WebSocket.Listener() {
                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        System.out.println(data);
                        return WebSocket.Listener.super.onText(webSocket, data, last);
                    }
                }).join(); // get websocket value when completes from buildAsync CompletableFuture
        while(true) {
            String input = scanner.nextLine();
            if("bye".equalsIgnoreCase(input)) {
                try {
                    webSocket.sendClose(WebSocket.NORMAL_CLOSURE,
                    "User left normally").get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    // Frames - communication messages or payloads, between websocket clients and server - basic unit of data sent over websocket
    // - data or control signals -
    // 1. Control frames
    // Data frames
    ///  can be split into multiple smaller frames - each transmitting separately -
    // Smaler frames are resassembled
    // default implementation for all WebSocket control methods - first parameter WebSocket type - java.net.http websocket
    // - once when builderSync method is exeucted on websocket builder - responsds with openControl frame
    // - error deleivered as part of srame
    // - all other methods
    // onPing, onpong - See slides formore details - Chapter - 408. - Networking
    // ---- use onBinary for images and audio
}
