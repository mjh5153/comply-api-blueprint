package com.init_spring_bean_mvn.demo.networking;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

public class UDProtocol {
    private static final int PORT = 5000;

    private static final int PACKET_SIZE = 1024;
    public static void main(String[] args) {
        // UDP Benefits - UDP - no handshading - destingation host, may or may not be a server - doesn't send a response to message sender
        // No accept or connect happens
        // Datagram Packet exchanges separately - unreliable and unordered so unreliable
        // Could broadcast its packets to many clients
        // time sensitive information and losing packets won't matter as much - speed more important
        // -- occasional packets not received and our eyes might not notice,
        // -- overhead involved with tcp is significant
        // -- DataGramSockets and DataGramChannels

        // Server downloads packets from audio files and client places audio

        // Swing

        // UDP sockets don't have to establish a persistent connection like tcp handshake
        try(DatagramSocket serverSocket = new DatagramSocket(PORT)) { // real world -use threads and non blocking features
            byte[] buffer = new byte[PACKET_SIZE];
            System.out.println("Wating...");
            DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(clientPacket);
            String audioFileName = new String(buffer, 0, clientPacket.getLength() );
            System.out.println("Client requested to listen to: " + audioFileName);
            // Next: client receive data incrementally playing it as received
            try {
                File audioFile = new File(audioFileName);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                System.out.println(audioInputStream.getFormat());
            } catch(UnsupportedAudioFileException ae) {
                System.out.println(ae.getMessage());
            }
            sendDataToClient(audioFileName, serverSocket, clientPacket);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }


    }

    private static void sendDataToClient(String file, DatagramSocket serverSocket, DatagramPacket clientPacket) {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        try(FileChannel fileChannel = FileChannel.open(Paths.get(file), StandardOpenOption.READ);
        ) {
            InetAddress cIP = clientPacket.getAddress();
            int clientPort = clientPacket.getPort();

            while(true)
            {
                buffer.clear();
                if(fileChannel.read(buffer) == -1) { // channel and buffer are different entities - when reading from channel you are writing to buffer that spplied it
                    break;
                }

                buffer.flip();

                while(buffer.hasRemaining()) {
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    DatagramPacket packet = new DatagramPacket(data, data.length, cIP, clientPort);
                    serverSocket.send(packet);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(22);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
