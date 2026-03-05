package com.init_spring_bean_mvn.demo.worker;

public class TextBlocksAdvancedOutputFormatting {
    // Skipped 98
    // 99 - Text blocks and advanced Output Formatting - multi0line string literals jdk 15

    public static void main(String[] args) {
        String bulletIt = "Print a Bulleted List: " + "\t\n\u2022 Item 1\n" + "\u2022\t\t Item 2\n" + "\t\t\u2022 Item 3";
        System.out.println(bulletIt);

        //multiline formatted string three quotes is a must
        String textBlock = """
                    Print a Bulleted List: \u2022 First Point
                            \u2022 Sub Point 
                """;

        System.out.println(textBlock);
        
        // FORMATTED AND PRINTING NUMBERS - printf instead of print or print ln - doesn't end with newline

        int age = 35;
        System.out.printf("Your age is %d%n", age);
        
        int yearOfB = 2023 - age; // %d used for integers, %f for floating point, %s for strings, %c for characters, %b for booleans
        System.out.printf(" Age = %d, Birth year = %d\n", age, yearOfB);
        // System.out.printf("Your age is %.2f%n", age); // compiles but get runtime exception - IllegalFormat Exception - need to caste age (an int) to a float
        //Exception in thread "main" java.util.IllegalFormatPrecisionException: 2
        //	at java.base/java.util.Formatter$FormatSpecifier.checkText(Formatter.java:3446)
        //	at java.base/java.util.Formatter$FormatSpecifier.check(Formatter.java:3141)
        //	at java.base/java.util.Formatter$FormatSpecifier.<init>(Formatter.java:3126)
        //	at java.base/java.util.Formatter$FormatSpecifierParser.parse(Formatter.java:2915)
        //	at java.base/java.util.Formatter.parse(Formatter.java:2814)
        //	at java.base/java.util.Formatter.format(Formatter.java:2744)
        //	at java.base/java.io.PrintStream.format(PrintStream.java:1183)

        System.out.printf("Your age is %.2f%n", (float) age);
        // reference - java.base/java/util/Formatter.html - d and f used most often - can specify width and precision for floating point numbers - %10.2f means 10 characters wide with 2 decimal places
        // %n outputs result is platform-specific line separator i.e. windows, mac or linux

        // String object has own format static method for formatting

        String fS = String.format("Your age is %d", age);
        System.out.println(fS);

        fS = "Your age is %d".formatted(age); // Can now do this shorthand on String Object
        System.out.println(fS);


    }

}
