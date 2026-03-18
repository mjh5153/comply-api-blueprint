package com.init_spring_bean_mvn.demo.javapackages;

import java.util.Scanner;

public class JavaPackages {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // need an import statement for this class - java.util package
        System.out.print("Enter a number: ");

        //Packages let us resuse common class names across different libraries or apps and provide a way to identity the correct class with either an import statement
        // package prefix - common practice as reverse domain of company or organization - com.init_spring_bean_mvn.demo.javapackages
        // package neeeds to be first in code before import, can only be one package statement

        // FQCN - fully qualified class name - java.util.Scanner - can be used without import statement but is more verbose
        // can use fqcn instead of import statement to avoid naming conflicts if two classes have same name in different packages - e.g. java.util.Date and java.sql.Date

        // Can use combination of import and fqcn
        // always specifiy package statement and avoid using default or unnamed package
        // can' import types of default package into other classes outside of default package


    }
}
