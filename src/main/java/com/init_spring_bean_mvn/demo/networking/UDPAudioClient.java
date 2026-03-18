package com.init_spring_bean_mvn.demo.networking;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;

import static java.awt.SystemColor.info;

/* if noise try adjusting the threads sleep time*/
public class UDPAudioClient {

    private static final int SERVER_PORT = 5000;
    private static final int PACKET_SIZE = 1024;
    public static void main(String[] args) {
        try(DatagramSocket clientSocket = new DatagramSocket()) {
            byte[] audioFileName = "AudioClip.wav".getBytes();
            System.out.println("Waiting for clickent");
            DatagramPacket packet1 = new DatagramPacket(audioFileName, audioFileName.length, InetAddress.getLocalHost(), SERVER_PORT);
            clientSocket.send(packet1);
            System.out.println("Client requested to listening ");
            playStreamedAudio(clientSocket);
        } catch(IOException | LineUnavailableException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void playStreamedAudio(DatagramSocket clientSocket) throws SocketException, LineUnavailableException {
        clientSocket.setSoTimeout(2000);
        AudioFormat format = new AudioFormat(22050, 16, 1, true, false); // contain info on how to play them - frequency -
        // little-endian - way bytes are ordered - javax.sound.sample.package and media library
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open();
        line.start();
        // get data from service to play
        byte[] buffer = new byte[PACKET_SIZE];
        while(true) { // continue to read data packets from server
            try{
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // create packet including buffer which includes audiofile
                clientSocket.receive(packet);
                line.write(buffer, 0, packet.getLength()); // clients desitination in packet -
                // Can do this in reverse to send to a server an audio  in chunks such as your voice
                // NEXT  change to DataGramChannel and practice event driven code

            } catch(IOException e) {
                System.out.println(e.getMessage());
                break;
            }
         }
        line.close(); // need to close source data line
    }
}
