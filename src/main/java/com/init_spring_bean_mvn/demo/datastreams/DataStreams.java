package com.init_spring_bean_mvn.demo.datastreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


class Player implements Serializable {
    private String name;
    private int topScore;
    private List<String> collectedWeapons = new ArrayList<>();
    public Player(String name, int topScore, List<String> collectedWeapons) {
        this.name = name;
        this.topScore = topScore;
        this.collectedWeapons = collectedWeapons;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", topScore=" + topScore +
                ", collectedWeapons=" + collectedWeapons +
                '}';
    }
}

public class DataStreams {
    // write primitive java data tyepes to output stream in portable way

    public static void main(String[] args) {
        Path dataFile = Path.of("data.data");
        writeData(dataFile);
        readData(dataFile);

        Player scan = new Player("Scan", 100_000_010,
                List.of("source", "user", "timestamp"));
        System.out.println(scan); // next need to write code that will write the object to an output stream

        Path scanFile = Path.of("scan.dat");
        writeObject(scanFile, scan);
        Player reconstitutedScan = readObject(scanFile);
        System.out.println(reconstitutedScan);
    }

    // instead of write methods by each type - writeInt, writeLong, etc....
    private static void writeObject(Path dataFile, Player player) {
        try (ObjectOutputStream objStream = new ObjectOutputStream( Files.newOutputStream(dataFile))
        ) {
            objStream.writeObject(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // read serialized player object back into memory from flat file from single readObject method call
    // * Much more in Serialization
    private static Player readObject(Path dataFile) {
        try( ObjectInputStream objStream = new ObjectInputStream(Files.newInputStream(dataFile))
        ) {
            return (Player) objStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeData(Path dataFile) {
        try (DataOutputStream dataStream = new DataOutputStream(
                new BufferedOutputStream( new FileOutputStream(dataFile.toFile()))
        )) {
            int myInt = 17;
            long myLong = 100_000_000_000_000L;
            boolean myBoolean = true;
            char myChar = 'Z';
            float myFloat = 77.7f;
            double myDouble = 98.6;
            String myString = "Hell world";

            long position = 0;
            dataStream.writeInt(myInt);
            System.out.println("writeInt writes "  + (dataStream.size() - position));
            position = dataStream.size();

            dataStream.writeLong(myLong);
            System.out.println("writeLong writes "  + (dataStream.size() - position));
            position = dataStream.size();

            dataStream.writeBoolean(myBoolean);
            System.out.println("writeBoolean writes "  + (dataStream.size() - position));
            position = dataStream.size();

            dataStream.writeFloat(myFloat);
            System.out.println("writeFloat writes "  + (dataStream.size() - position));
            position = dataStream.size();

            dataStream.writeChar(myChar);
            System.out.println("writeBoolean writes "  + (dataStream.size() - position));
            position = dataStream.size();

            dataStream.writeDouble(myDouble);
            System.out.println("writeDouble writes "  + (dataStream.size() - position));
            position = dataStream.size();

            dataStream.writeUTF(myString); // uses first two bytes to write character
            System.out.println("writeString writes "  + (dataStream.size() - position));
            position = dataStream.size();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e); // can remove because it's a child
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // serialization allows for translating a data structure or object, into a format that can be stored on file
    // - Only instances of Serializable classes can be serialized, class needs to implement Serializable interface.
    // - allows below to not have to be done
    // No methods - Marks class as
    // All subtypes of serializable class are also serializable

    // default mechaniz writes class of object, class signature, non-static fields,
    // Used to restore object and statie during read operation
    // reconsitituting data or SERIALIZATION
    private static void readData(Path dataFile) {
        try(DataInputStream dataStream = new DataInputStream(
                Files.newInputStream(dataFile))) {
            System.out.println("myInt =  " + dataStream.readInt());
            System.out.println("myLong =  " + dataStream.readLong());
            System.out.println("myBool =  " + dataStream.readBoolean());
            System.out.println("myFloat =  " + dataStream.readFloat());
            System.out.println("myChar =  " + dataStream.readChar());
            System.out.println("myDouble =  " + dataStream.readDouble());
            System.out.println("myUTF =  " + dataStream.readUTF());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
