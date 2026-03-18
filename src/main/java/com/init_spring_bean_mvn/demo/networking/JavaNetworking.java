package com.init_spring_bean_mvn.demo.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaNetworking {
    public static void main(String[] args) {
        // Network of Networks
        // Java - Supported since first version

        // Pardimgs
        // Original java .net - types: sockets and addresses
        // Higher level types - hide or manage connections level details

        // 2. New changes: File.io Channels
        // Robust way of communictating between interrconnected devices - sever channels
        // 3. JDK11 - java.net.http - HTML 2.0 , build patterns to creates types specificyy for internet comm
        // 4. websocket type - persistent connections - enabling bi-directional communications

        // B. Networking Terminology: Exchange data and share resources -
        // 2 ways: LAN and internet
        // 1. data exchange data in real-time, distributed systems , remote access and control to distributed systems,
        // - communication between apps like apis and web services - Java Package enables

        // A. Terminology - Private Network ( Intranet ) - networking began: office computers share computers over business intranet
        //  colloborative dev and file shares
        // 2. Host - vm,
        // 3. config - client server: clients connect to server ( how internet operates )
        //  a. gaming oconsole: clients
        // 4. Server - powerful computers or apps that interact and respond with data or action requested
        // a. Client server model - web applications, email, file sharing and gaming - Web browsing
        // 5. Singl host: ex: msql db and workbench connecting as mysql database server
        // a. web server running on your maching as web developer
        // 6. communication - severl layers - network layer - data transport layer - delivery system
        // a. port - physical connection - routed to target through port - eahca pp assigned a port
        // licnts connections to server on same machine,
        // data arrives port number used to route data -
        // c. integer valu e0 to 60000
        // 7. IP Address - can find out with whatismyip.com or command line or iplocation.net/find-ip-address
        // a. ipv6 - series of 8 segments
        // b. ipv4: 32 bit address scheme - 4 billion unique addresses ( not enough ) ipv6 born
        // c. ipv6: 128 bit address scheme - isp's provide ipv4 and ipv6 addresses to customers - compatible with old and new
        // 8. Data transfer layer - different protocals: TCP, UDP: establishes and maintains connnections between hosts, udp: connectionless
        // a. less reliable and no order guarantee, continuous stream, indempendant unrelated packats
        // b. TCP has error checking for dropped packets: slower
        // c. udp - spead more important- online gaming , streaming media and real time data feeds
        // 9. TCP/IP - transerring data over ip addresses - two apps running on same host can use tcp ip to communicate with each other - 1.0.0.127
        // 10: High level and low level: java.nio.channels, java.net, java.net.http - see slipds - low level aps with data gram protocals - ALWAYS USING ABSTRACTIONS
        // 1Sockets: Low level api - one enpoint of two-way connection : client and server - use same port but have own socket - SocketClass

        // 11. Overview - Two applications will be written:
        // a. Client/Server

        /*385 - TCP/IP ServerSocket Client Server App*/

        // Purpose
        try(ServerSocket sst = new ServerSocket(5000)) {
            // can consult wikipedia to get alternate number if needed
            try (Socket socket = sst.accept();) { // only first connection will be accepted,
                // Want to make server multiple connections, simaltaneously
                // Need to make program multithreaded
                // Next - code will need to be modified to accept multiple clients
                System.out.println("Server taking requests ");

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true); //io streams will get closed by outer try catch
// closing server socket just won't accept new connections
                while(true) { // waiting for client to connect on port 5000
                    String echoString = input.readLine();

                    if(echoString.equals("exit")) {
                        break;
                    }
                    output.println("Echo from server: " + echoString); // block here unless terminate application
                    // How to solve without threading
                }
            }
        } catch (IOException e) {

            System.out.println("Server excetpion " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
