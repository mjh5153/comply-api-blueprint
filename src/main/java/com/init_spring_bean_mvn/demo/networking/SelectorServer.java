package com.init_spring_bean_mvn.demo.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorServer {
    public static void main(String[] args) {

        // Handles multiple clients with multiple requests WITHOUT Multi-threading
        try(ServerSocketChannel sc = ServerSocketChannel.open()) {
            sc.bind(new InetSocketAddress(5000));
            sc.configureBlocking(false);
            Selector selector = Selector.open();
            sc.register(selector, SelectionKey.OP_ACCEPT); // PASS CHANNEL EVENT, creates key - Java call registration token, bridge between channel and program
            // select wakes up program, provides key for channel who registered with event;
            while(true) {
                selector.select(); // blocking by default -
                Set<SelectionKey> sk = selector.selectedKeys();
                Iterator<SelectionKey> iterator = sk.iterator();
                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isAcceptable()) {
                        SocketChannel clientChannel = sc.accept();
                        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        // react when key gets returned
                    } else if(key.isReadable()) {
                        // tru clickent has mad request
                        echoData(key);
                    }
                }
            }
        } catch (IOException io) {

        }
    }
    private static void echoData(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
         int bytesRead = clientChannel.read(buffer);
         if(bytesRead>0) {
             buffer.flip();
             byte[] data = new byte[buffer.remaining()];
             buffer.get(data);
             String message = "Echo: " + new String(data); // can test for characterset by
             clientChannel.write(ByteBuffer.wrap(message.getBytes()));
         } else if (bytesRead == -1) {
             System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
             key.cancel();
             clientChannel.close();
         }
    }
}
