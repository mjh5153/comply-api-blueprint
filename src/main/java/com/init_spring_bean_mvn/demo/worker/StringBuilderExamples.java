package com.init_spring_bean_mvn.demo.worker;

public class StringBuilderExamples {
    // mutable class to change java's string sequence

    public static void main(String[] args) {
        StringBuilder // 4 overloaded constructors - default, with capacity, with string, with char sequence
                sb = new StringBuilder("Worker");

        String worker = "Hello " + " Worker ";
                //can't assign a string literal to a string builder variable

        StringBuilder workerBuilder = new StringBuilder("Hello - " + worker);

        // stringbuilder message append = string concat method
        worker.concat(" - Welcome to the team!"); // creates new string object in memory with concat result but doesn't change original worker string value
        workerBuilder.append(" - Welcome to the team!");

        // string literal concat creates object in memery for that literal - didn't assign concat result to variable so it is not stored in memory and is lost after that line of code is executed
        // string builder append method changes the value of the string builder object in memory and doesn't create a new object for each append call - more efficient for multiple string manipulations
        // StringBuilder methods return a StringBuilder reference but it's really a self-reference to support chaining methods together

        StringBuilder emptyStart = new StringBuilder(32); // default constructor creates empty string builder with capacity of 16 characters
        // data needs to get copied over to larger storage area
        emptyStart.append("a".repeat(34)); // capacity is 32 characters now because it needs to resize to accommodate the new string value
        // delete, insert, reverse, setLenth - truncate or expand string builder, delete characters, insert characters at specific index, reverse the string builder value

        emptyStart.deleteCharAt(16).insert(16, "b"); // delete character at index 16 and insert b at index 16 - can chain methods together because they return a reference to the same string builder object
        System.out.println(emptyStart);

    }
    public static void printInformation(String string) {

    }

    public static void printInformation(StringBuilder builder) {
        System.out.println(builder.capacity()); // capacity is the amount of characters the string builder can hold before it needs to resize - default is 16 characters
    }
}
