package com.init_spring_bean_mvn.demo.randomfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
// use ra file to create binary data file that has index mechanism to locate data records
// used indexed binary data file to read data
// ONLY couple of use cases - didn't have to cache all records, just index
// random accessed file - targeted data modifications for large file: ex: 777 from DATA.idx file to locate data
// binary data storage
public class JavaRandomAccessFile {
    // directly access and modify data in any file, array of file bytes
    // cursor into file array called pointer
    // Read and write operations
    // open? file pointer = 0, move file pointer call method called seek to move to desired part of file
    // getFilePointer, file pointer will move certain number of bytes when file completes
    // read methods - every primitive type, readLines, readUTF - read string from file, same for write
    // same for write's

    // Uses - Man millions of records ? Can load a small list. RandomAccessFile lets you read in specified positions of files with seek
    // need to know what files are
    // index which houses file pointer position to each record of interest
    // file - fixed width - data by row number
    // More common to retrieve record by non sequential id than row id

    // You will need an index with id and position with in file of record associated with id
    // For fixed with - index wouldn't need file pointer position- rowId -> fileId - example array of ids - row id sequence

    // variable length: store length of each record or store starting file pointer position
    // index: store record id : associated file pointer
    // Fixed Width -> may not exist or at same place of variable - beginning of data file or after or separate file

    private static final Map<Long, Long> indexedIds = new LinkedHashMap<>();
    private static int recordInFile = 0;

    //
    static {
        try(RandomAccessFile rb = new RandomAccessFile("Dataidx", "r")) {
            loadIndex(rb, 0);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    public static void main (String[] args) {
        // BuildData.build("Data", true); // change code to use this new index file
        // NEXT binary data file and associated index - next video
        // DataOutputStream
        // DataInputStream
        try (RandomAccessFile ra = new RandomAccessFile("files/employees.dat", "r")) {
            loadIndex(ra, 0);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a Delegate Id or 0 to quit");
            while (scanner.hasNext()) {
                long scanId = Long.parseLong(scanner.nextLine());
                if(scanId < 1) {
                    break;
                }
                ra.seek(indexedIds.get(scanId));
                String targetedRecord = ra.readUTF(); // writeUtf - include length of data written so readtf can read that amount into block written
                System.out.println(targetedRecord);
                System.out.println("Enter a Delegate Id or 0 to quit");
                // could use max and minus function to get key ranges and values
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void loadIndex(RandomAccessFile ra, int iP) {
            try {
                ra.seek(iP);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                recordInFile = ra.readInt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(recordInFile);
            for(int i = 0; i < recordInFile; i++) {
                try {
                    indexedIds.put(ra.readLong(), ra.readLong());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    }
}
