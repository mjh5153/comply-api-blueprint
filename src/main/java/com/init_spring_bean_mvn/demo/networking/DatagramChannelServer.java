package com.init_spring_bean_mvn.demo.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class DatagramChannelServer {
    private static final int PORT = 5000;
    private static final int PACKET_SIZE = 1024;

    public static void main(String[] args) {
        try(DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(PORT));
            System.out.println("Server listening on port " + PORT);

            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);

            while(true) { // allows to continue to listen to events
                selector.select();
                var selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> kI = selectedKeys.iterator();
                while(kI.hasNext()) {
                    SelectionKey key = kI.next();
                    kI.remove();

                    if(key.isReadable()) {
                        var registeredChannel = (DatagramChannel) key.channel();
                        buffer.clear();
                        var clientAddress = registeredChannel.receive(buffer);
                        buffer.flip();
                        byte[]data = new byte[buffer.remaining()];
                        buffer.get(data);
                        String audioFilePath = new String(data);
                        System.out.println("Client requested to listen to :" + audioFilePath);
                        new Thread(() -> sendDataToClient(audioFilePath, clientAddress, registeredChannel)).start();

                        // Data channel is open, but not connected
                        // doesn't need to be connected in order for send and receive methods to be used -
                        // dc may be connected, by incoking its connect method to avoid overhead of security checks - would be performed as part of send and receive operation for every operation -
                        // read and write methods: datagram channel must be connected

                        // NEXT - sockets, ports and ip addresses hidden in higher level java api's

                    }
                }
            }
        } catch( IOException e) {
            throw new RuntimeException(e);
        }
    }

    // sends audio packets
    private static void sendDataToClient(String file, SocketAddress address, DatagramChannel channel) {
        // file channel is autoclosealbe

        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        try(FileChannel fileChannel = FileChannel.open(Paths.get(file), StandardOpenOption.READ);
        ) {

            while(true) {
                buffer.clear();
                int bytesRead = fileChannel.read(buffer);
                if(bytesRead == -1) {
                    break;
                }

                buffer.flip();
                while(buffer.hasRemaining()) {
                    channel.send(buffer, address); // send method on datagram channle - send
                    // didn'thave to create or open a certain type of Input stream to get data and then create and open output stream to pipe data out

                    // Channels can use same data container and transfer data to difference channel - Beauty of buffer and advantages of using channels
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(22);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch(IOException e ) {
                System.out.println(e.getMessage());
            }
        }
    }

