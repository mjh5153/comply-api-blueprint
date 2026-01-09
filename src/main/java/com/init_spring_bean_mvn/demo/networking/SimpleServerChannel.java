package com.init_spring_bean_mvn.demo.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
// Channels - interface , part of java.nio channels package - open connection to some entity
// input and output -
// DatagramChannel, File Channel and other
// Think of Data pathway
// channel - constructed by static method, open on channel impl
// Under Channel - socket is also open

// Buffer - abstract class that's part of
// data added to buffer using put methods
// Channels write method takes buffered input and transers it to connected entity

// java.nio.Buffer not same as buffers for io fuffered stream
///  Buffer ca be both readable and wrteable - State changed by flip method
/// // jaster full bulk transfers b/c minimizes system calls, fine grained control, lowwel io and working with channels
///
// Capacity,        limit                   position
// upper limit      Can't exceed capacity   indicates capacity in use
// can't be
// created later
// after set on
// creation

    // ByteBuffer                           vs CharBuffer
    // w/o inheritant character encoding        Extra layer of encoding and decoding - impacting performance
    // efficient data transfer
    // socket channels operate by byte
    // buffer

// 390 - Increase channel per client to > 1
    // can create new thread and asynchronous with a while loop
    // with code below we'd be stuck in busy client loop - not useful for m

    /* Polling Solution - continually check for new requests
    *  May not be as useful on high traffic systems, more sophisticated design and more infrastructure
    * Much more scalable than blocking solutions
    *
    * Next - Event Driven : message queues, client server ommunications, User events: button clicks */
    // Selectors - two types of workers: one running around for something to do, 2. Continually doing regular tasks: can handle more important events when needed
    // register interest in event, system reacts when events trieggered and shares notifications with interested parties
    // loosely coupled and operate independently
    // firing of and listening for events
    // ansychronous actions can be registered i.e Promis is some languages
    // system can listen for completed response event
    // Events drive action rather than looping indefinitly - wastes resrouces in process

    // I.E Selectable Channels - io endpoints like sockets, pipes and file channesl

    // Selector : selectables channels - key to event drive dev in io
    // monitor set of channels for readiness for spercific events
    // called interests - register channel has specfici event - registration is done visa selector
    // Server can do other tasks while selector waits for event - program then reacts to event in some way
public class SimpleServerChannel {
    public static void main(String[] args) {
        try(ServerSocketChannel serverChannel = ServerSocketChannel.open()) { // constructs new server socket channle to listen to incoming connections
            // bind internal socket to host and post,

            serverChannel.socket().bind(new InetSocketAddress(5000));
            serverChannel.configureBlocking(false); // returns with new client connection or null;

            System.out.println("Server is listing on port" + serverChannel.socket().getLocalPort());
            // sets up client connection

            List<SocketChannel> clientChannels = new ArrayList<>();
            while(true) { // currently doesn't continually accept requests from multiple clients - subsequent requests to same client hangs

                // Can handle with threads but additional solutions but first channels and nio buffers
                SocketChannel clientChannel = serverChannel.accept();
                if(clientChannel != null) {
                    clientChannel.configureBlocking(false);
                    clientChannels.add(clientChannel);
                    // socket channels use buffers - nio - to pass data around, not same as input and output streams
                    // data - exchanged in chunks, define how big - capacity on buffer
                    // byteBuffer:
                }

                ByteBuffer bf = ByteBuffer.allocate(1024);
                for (int i = 0; i < clientChannels.size(); i++) {
                    // harddisk sectors, communication protocals and other reasons
                    SocketChannel c = clientChannels.get(i); // next need to cfix blocking code - before accept first client

                int readBytes = c.read(bf);

                if (readBytes > 0) {
                    // direct access and control over buffer but manally manuipulate

                    // Channel writes, now read

                    bf.flip();
                    c.write(ByteBuffer.wrap("Echo from server:".getBytes()));
                    while (bf.hasRemaining()) {
                        c.write(bf); // write back to channel
                    }
                    bf.clear(); // check for -1 from readChannel - timed out without receiving data: client drops connection
                } else if (readBytes == -1) {
                    System.out.printf("Connection to %s lost%n", c.socket().getRemoteSocketAddress());
                    c.close();
                    clientChannels.remove(i);
                }
            }
            }
        } catch(IOException e) {
            System.out.println("Server Exception " + e.getMessage());
        }
    }
}
