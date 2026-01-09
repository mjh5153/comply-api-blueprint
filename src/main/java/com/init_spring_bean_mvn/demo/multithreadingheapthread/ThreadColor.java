package com.init_spring_bean_mvn.demo.multithreadingheapthread;

public enum ThreadColor {

    // define string making all output be of that color
    // special ansi characters that will change color of consols text

    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),

    ANSI_WHITE("\u001B[37m"),

    ANSI_BLUE("\u001B[34m"),

    ANSI_CYAN("\u001B[36m"),

    ANSI_GREEN("\u001B[32m"),

    ANSI_PURPLE("\u001B[35m"),

    ANSI_RED("\u001B[31m");

    private final String color; // consist of ANSII numerized characters



    ThreadColor(String color) {
        this.color = color;
    }
    // accessor method way of getting color variable
    public String color() {
        return color;
    }

}
