package com.init_spring_bean_mvn.demo.randomfile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildData {

    public static void build(String datFileName, boolean separateIndex) {
        Path studentJson = Path.of("Data.json");
        String dataFile = datFileName + ".dat";

        Map<Long, Long> indexedIds = new LinkedHashMap<>(); // for insertion order matching order or records printing in // keep track of file position of each record
        // as write out each file position,
        // all records inserted and index map complete, write total number of records at position 0
        // start outputting each indexed key value pair
        // Can write before adding index because I'm using a random access file and can move to position I want to write
        // Can derive the position to start by calculating 4 bytes to store record count -> integer output
        // 16 bytes for indexed entry i.e long = 8 bytes
        // 1000 * 16 + 4

        try {
            Files.deleteIfExists(Path.of(dataFile));
            String data = Files.readString(studentJson);
            data = data.replaceFirst("^(\\[)", "")
                    .replaceFirst("(\\])$", "");
            var records = data.split(System.lineSeparator());
            System.out.println("number of records = " + records.length);

            long startingPos = 4 + (16L * records.length);

            // extract id from each record
            Pattern idPat = Pattern.compile("studentId\":([0-9]+)");
            try (RandomAccessFile ra = new RandomAccessFile(dataFile, "rw")) { // read write
ra.seek(startingPos);
                for(String record : records) {
                    Matcher m = idPat.matcher(record);// java.util.Regex
                    if (m.find()) {
                        long id = Long.parseLong(m.group(1));
                        indexedIds.put(id, ra.getFilePointer());
                        ra.writeUTF(record);
                    }
                }
                // writeIndex(ra, indexedIds);
                writeIndex((separateIndex) ? new RandomAccessFile(datFileName + "idx", "rw") : ra, indexedIds); // rw - need to read and write from index file that code generates
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeIndex(RandomAccessFile ra, Map<Long, Long> indexMap) {
        try {
            ra.seek(0);
            ra.writeInt(indexMap.size());
            for(var studentIdx : indexMap.entrySet()) {
                ra.writeLong(studentIdx.getKey());
                ra.writeLong(studentIdx.getValue());
            }
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
    }
}
