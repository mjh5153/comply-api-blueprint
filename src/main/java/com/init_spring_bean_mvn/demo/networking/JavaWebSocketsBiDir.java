package com.init_spring_bean_mvn.demo.networking;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JavaWebSocketsBiDir extends WebSocketServer {

    public static final int SERVER_PORT = 8080;

    private static Map<String, String> map = new HashMap<>();
    public JavaWebSocketsBiDir() {
        super(new InetSocketAddress(SERVER_PORT));
    }

    public static void main(String[] args) {
        var server = new JavaWebSocketsBiDir();
        server.start();
    }
    // isn't java.net.http.net - Library own type providing serverside functionality for us

    // below show lifecycle methods of clients
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        // used to start up websocket server
        var resource = webSocket.getResourceDescriptor();
        String name = resource.split("=")[1];
        map.put(webSocket.getRemoteSocketAddress().toString(), name);
        // adds entry to map keyed on sockets websocket address
        System.out.println(map.values());
        System.out.println("Connection opened " + webSocket.getRemoteSocketAddress());
        broadcastAllButSender(webSocket, "%s joined".formatted(name));
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Connection opened " + webSocket.getRemoteSocketAddress());

    }
    private void broadcastAllButSender(WebSocket webSocket, String message) {
        var connections = new ArrayList<>(getConnections());
        connections.remove(webSocket);
        broadcast(message, connections); // by default will broadcast to all connections
    }
    @Override
    public void onMessage(WebSocket webSocket, String s) {

        String chatName = map.get(webSocket.getRemoteSocketAddress().toString());
        broadcastAllButSender(webSocket, "%s: %s".formatted(chatName, s));
        System.out.println("Connection opened " + webSocket.getRemoteSocketAddress());

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("Connection opened " + webSocket.getRemoteSocketAddress());

    }

    @Override
    public void onStart() {
        System.out.println("Connection opened " + getPort()); // Class inherits this class

    }

    // Client in HTTP one-directional - needs periodic requests, pooll the server regurulalay checking for updates

    // Websocket Communication is bi-directional - persistent connection between server and client - send massesages back and forth - chat ,
    // socket tickers, and colaborative tooled
    // Pushed to client with establishing new collections

    // Websockets leverage of the reliability of tcp

    // establish a http handlshake

    // 1. Upgrade header on request

            // hanshake paves way for upgrade to WebSocket protocol istself - real time data exchange , overhead and latency
    // 2. requeire special handshake mechanism - binray framing format used - text neds to be encoded bfoere sending and decoded on receiving end - see mozilla developer site
    // /en-US/docs/Web/API/Writing_a_WebSocket_server_in_Java - RFC 6455 - describes websocket in detail from browser
    // -- describes communication of TCP - GET request sent to upgrade connection - Handshake - Header key - Sec-WebSocket0Key request header
    // header much send request back with Sec-WebSocket-Accept response header as part of response

    // 4. decoding message - encoded in opcodes - see java.net.http package WebSocket class - WebSocket client : Connect to and communicating with existing web servers
    // without handshake and opcodes -

    // Lesson - Set up websocket server - opensource java.websocket.Java.WebSocket v1.5.6
}
