package com.init_spring_bean_mvn.demo.worker;

public class StringMethodsAndBPs {
    public static void main(String[] args) {

        // String has over 60 methods
        // string character array starts at index 0
        // split strings methods - string inspection, compareing, manipulation

        // 1. length, charAt, indexOf, lastIndexOf, isEmpty, isBlank - jdk 11

        // isBlank - checks if the string is empty or contains only whitespace characters
        printIF("Worker Data");
        printIF("\t \n"); // string with tabs and newlines, isBlank will return true, isEmpty will return false

        String Worker = "Sliver";
        System.out.printf("index of r= %d %n", Worker.indexOf('r')); // index of r= 3
        System.out.printf("lst index index of r= %d %n", Worker.lastIndexOf('r'));
        // can pass second parameter to indexOf and lastIndexOf to specify the starting index for the search
        System.out.printf("lst index index of r= %d %n", Worker.lastIndexOf('i', 3));

        // equals
        String WorkerLower = Worker.toLowerCase();
        if(WorkerLower.equals(Worker)) {
            System.out.println("Strings are equal");
        } else {
            System.out.println("Strings are not equal");
        }

        // contentEquals - can be used to compare StringBuilders value which equals doesn't support
        if(WorkerLower.contentEquals(Worker)) {
            System.out.println("Strings are equal");
        } else {
            System.out.println("Strings are not equal");
        }


        // String manipulation methods next video - 101
        // indent, strip, {stripLeading, stripTraining} jdk11, trim, toLowerCase, toUpperCase,
        // repeat, replace, replaceAll, replaceFirst, substring, split, join, concat, subString, subSequence ( start and end of index specified )

        String birthdate = "25/11/1982";
        int StartingIndex = birthdate.indexOf("1982"); // index of first slash + 1 to get start of month

        System.out.println("Birth year = " + birthdate.substring(StartingIndex)); // substring from starting index to end of string
        System.out.println(birthdate);
        // overloaded version getting index as well

        System.out.println("Birth year = " + birthdate.substring(StartingIndex, StartingIndex + 4)); // substring from starting index to end of string

        String newDate = String.join("-", "25", "11", "1982"); // join method takes a delimiter and varargs of strings to join
        System.out.println(newDate);

        // comma delimited string to array of strings using concat

        newDate = "25";
        newDate = newDate.concat(",").concat("11").concat(",").concat("1982"); // can write this way because the concat returns a string
        System.out.println(newDate);

        // less efficient with string literals
        newDate = "25" + "," + "11" + "," + "1982"; // more efficient because java recognize the use of literals

        System.out.println(newDate);

        // ** a better class to use is the StringBuilder
        // ** Next replace characters methods

        System.out.println(newDate.replace('-', '/')); // replace all occurrences of / with -
        System.out.println(newDate); 
        // can use replace to replace all of a character with other characters
        // replaceFirst and replaceAll - use regular expressions to specify the pattern to replace
        // replaceFirst replaces the first occurrence of the pattern, replaceAll replaces all occurrences of the pattern
         newDate = "25-11-1982";
         System.out.println(newDate.replaceFirst("-", "/")); // replace first occurrence of - with /

        //repeat jdk11
            String repeated = "Worker".repeat(3); // repeat the string 3 times, can be chained with indent
                // repeat can be negated to remove spaces for example - "   Worker   ".repeat(-1) will remove leading and trailing spaces
            System.out.println(repeated);
        // repeatAdded jdk15



    }
    public static void manipulateStrings(String string) {

    }
    public static void printIF(String string) {
        int length = string.length();
        System.out.printf("Length = %d %n", length);
        char firstChar = string.charAt(0);
        System.out.printf("First character = %c %n", firstChar);

        // isEmpty - checks if the string is empty (length is 0) string.length will be 0
        // characters could be blank - tabs, newlines etc...

        if(string.isEmpty()) {
            System.out.println("String is empty");
        } else {
            System.out.println("String is not empty");
        }
    }

    // compareTo and matches are advanced methods


}
