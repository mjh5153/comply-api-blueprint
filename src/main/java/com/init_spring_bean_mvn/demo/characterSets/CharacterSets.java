package com.init_spring_bean_mvn.demo.characterSets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CharacterSets {

    // glyphs - assigning numbers to various characters - represent with numeric, alphabetic, in any language, punctiontion or emojiis
    // US-ascii - 7 bits, ISO-8859-1 - 8 bits : widely supported, Latin Aplphabet

    // utf-8, 16, 32: 1 to 4,, 2 bytes, 4 bytes: most populater - all writing systems, UTF-32 - more efficient and straightforward to procoess but more disk space
    // UTF-8 better choice - wider range of characters but depends on use case if less characters needed - see StandardCharsets.html
    public static void main (String[] args) {
        // Check default character encoiding
        System.out.println(System.getProperty("file.encoding"));
        // can override - default is UTF-8
        System.out.println(Charset.defaultCharset()); // data gets encoded to characters from bytes if data is text

        Path path = Path.of("files/ScanResultsFixed.csv");


        try {

            Pattern p = Pattern.compile("(.{15})(.{3})(.{12})(.{8})(.{2}).*");
            Set<String> values = new TreeSet<>(); // Use set to remove dups and treeset to be sorted
            // read data in with Files.readAllLines
            Files.readAllLines(path).forEach(s -> {
                if(!s.startsWith("Name")) {
                    Matcher m = p.matcher(s);
                    if(m.matches()) {
                        values.add(m.group(3).trim()); // group of match and trim
                    }
                }
            });

            // streams lazily executed: opened and never closed until terminal operation applied to stream
            try (var stringStream = Files.lines(path)) { // similar to stream of paths - needed to wrap in try with resources
                var results = stringStream
                        .skip(1)// skip header row
                        .map(p::matcher)
                        .filter(Matcher::matches) // all data may not be formatted
                        .map(m -> m.group(3).trim())
                        .distinct()
                        .sorted()
                        .toArray(String[]::new);
                        System.out.println(Arrays.toString(results));
            }
            System.out.println(values);
            System.out.println(new String(Files.readAllBytes(path)));
            // 2 gb in size - readAllBytesd, readString, readAllLines, lines - All read entire text files - readAllLines: String, lines: Stream source of string and included in try with resources block
            // > 2 gb bufferedReader or channel
            try (var stringStream = Files.lines(path)) { // similar to stream of paths - needed to wrap in try with resources
                var results = stringStream
                        .skip(1)// skip header row
                        .map(p::matcher)
                        .filter(Matcher::matches) // all data may not be formatted
                        .collect(Collectors.groupingBy(m -> m.group(2).trim(), Collectors.counting())); // get mapped keyed by stream and name

                results.entrySet().forEach(System.out::println);
            }
            System.out.println(values);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
