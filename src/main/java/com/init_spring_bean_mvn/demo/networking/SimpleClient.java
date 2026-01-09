package com.init_spring_bean_mvn.demo.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Only connects to single client - can add more with edit configurations
public class SimpleClient {
    public static void main(String[] args) {
        try(Socket socket = new Socket("127.0.0.1", 5000)) { // localhost alias to 127.0.0.1 i.e for macos needed 127 ip to connect to JavaNetworking - Server from here: Client
            // once have socket, now have communication with click

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);
            String requestString;
            String responseString;
do { // always send at least one request
    System.out.println("Enter string to be echoed ");

    requestString = sc.nextLine();
    output.println(requestString);
    if (!requestString.equals("exit")) {
        responseString = input.readLine();
        System.out.println(responseString);
    }
} while (!requestString.equals("exit"));

        } catch(IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        } finally {
            System.out.println("Client Disconnected");
            // can run both apps now
        }
    }
}
