package com.init_spring_bean_mvn.demo.networking;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        // Understand state of buffer
        // different operations effect it
        Consumer<ByteBuffer> pf = (buffer) -> {
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            System.out.printf("\"%s\" " , new String(data, StandardCharsets.UTF_8));

        };
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        doOperation("Print: ", buffer, (b) -> System.out.print(b+ " "));
        // write text to buffer
        doOperation("Write", buffer, b -> b.put("this is a test".getBytes()));

        // can compare remaining buffer space after write or b.put - i.e bytes written to buffer
        // Capacity - position

        // Next change state -
        doOperation("Flip ", buffer, ByteBuffer::flip); // Commenting out will give BufferUnderflowException -
        // reaidng at inddex of position: 14 , read reamining 1010 bytes, when reads index at 1024, i.e get exception
        // Happens usually when forgetting to flip buffer
        // limit will change and posistion and remaining to 14 - last index or remaining in byte index

        // print buffer consumer variable
        doOperation("read and Print Value: ", buffer, pf); // limit did not change: length of data in buffer
        // position changed 0 to 14 - remaining is 0

        doOperation("Flip: ", buffer, ByteBuffer::flip); // Limit gets set to previous position state: 14, Current position set to 0
        // limit gets set to 0 in both flips

        // Change psotion of buffer
        // doOperation("1. Move position to end of text", buffer, (b) -> b.position(b.limit()));
        // doOperation("2. Change limit to capacity ", buffer, (b) -> b.limit(b.capacity()));
        // will get exception on next put - Append
        // Now append should be successful - position: 14 to 33
        // Simpller way ----------- Compact method
        doOperation("Append: compact ", buffer, ByteBuffer::compact);
        doOperation("Append: ", buffer, b-> b.put("This is a new test".getBytes()));
        // BufferOverlow Exception - Fix: set position to end of text

        // doOperation("Flip ", buffer, ByteBuffer::flip); // Commenting out will give BufferUnderflowException -
        // reaidng at inddex of position: 14 , read reamining 1010 bytes, when reads index at 1024, i.e get exception
        // Happens usually when forgetting to flip buffer
        // limit will change and posistion and remaining to 14 - last index or remaining in byte index

        // print buffer consumer variable
        // doOperation("read and Print Value: ", buffer, pf); // limit did not change: length of data in buffer
        doOperation("read and Print Value: ", buffer.slice(0, buffer.position()), pf); // get write data to print out without flip
        // backed by same byte array, can read from second buffer,

        // next append to butter - copy read and print value line
        // append data without doing read and write
        // shared subsequence in new buffer reference

        // debugging - examine state before and after a method - doOperation,
        // duplicate buffers, read only buffers, help slice and dice data we're passing around

        // NEXT - Non-blocking socket channel
    }
    // buffer and consumer parameter ( operation call on buffer method ) - reduce code writing | decofating

    private static void doOperation(String op, ByteBuffer buffer, Consumer<ByteBuffer> c) {
        System.out.printf("%-30s", op);
        c.accept(buffer);
        System.out.printf("Capacity = %d, Limit = %d, Position = %d, Reamining = %d%n", buffer.capacity(),
                buffer.limit(),
                buffer.position(),
                buffer.remaining());
    }

}
