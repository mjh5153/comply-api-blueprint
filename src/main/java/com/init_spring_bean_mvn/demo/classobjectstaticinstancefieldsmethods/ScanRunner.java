package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

import com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods.Scan;

public class ScanRunner {
    public static void main(String[] args) {
        Scan scan = new Scan("", "", "");
        // scan.name // can't do - fields have private access

        // How does outside code interact with fields of a class? - through methods
        // Don't - make fields public, instead make them private and provide public methods to access and modify the fields - getters and setters
        // DO - make getters and setters: controls and protects access to private fields
        // signatures are part of scans interface - can change name or type of field and changes should have no effect of external code to our class
        // Code maintains enclosure from outside class

        // Setters can validate data - ensure immutability, check dtata requirements - validates state

        // ex: setScanId(String scanId) {




        // Getters, encapsulation and object Access
        // null is special keyword
        // // a field with a primitive data type will be assigned a default value by java

        // boolean: false, byete, short, int l long, char: 0, double float: 0.0, reference data type: null


        // Challenge - Build a bank account

        // Use constructor to not use setters for each field - specifiy access modifier for who can create the fields for instance of class
        //  Can add other initialization logic in constructor - validate data, set default values, etc... - can have multiple constructors with different signatures
        //  Constructor created in byte code during compilation process by java, i.e. new - default constructor
        // no arguments constructor - has no parameters - only called once, class can have one or multiple ocnstructors which one could be no args
        // common practice is to make field names same as parameter names, passed to constructor
        // Other access modifiers - Overloading constructors - multiple constructors with different signatures - can have one constructor call another constructor using this() keyword - constructor chaining

        // 85. When to call overloading constructors
        // 6. Challenge - building customer data
        // - Constructor chaining - call one constructor from another using this() keyword - constructor chaining
        //  - this keyword (MUST be first line in constructor) must be first executable statement if used from another constructor
        // - can be used to avoid code duplication and improve maintainability of code - the constructor called determined by aruments passed
        // - types must also match: ex: this(number, balance, etc...)

        // Always better to asign value to field instead of using setters in constructor - ensures immutability and validation of data at time of object creation

        // Intellij has shortcut to generate constructors, getters and setters - can select which fields to include in constructor and which to include in getters and setters

        // name, address, email, setters and getters, create constrcuctor for three fields, create no-args constructor that calla nother constructor, create a constructor with just name and email parameters that calls another constructor
        //  ----------------------------------    Confident I can do this, skipping ----------------------------

        // 86. References, Objects and Instances - Ojbect and instances are interchangeable
        // 88. Static methods and instance methods  - statc methods declared using a static modifier ,
        // static method can't use the this keyword because they don't belong to an instance of the class, they belong to the class itself - can't access instance fields or methods directly from static method - can only access static fields and methods directly from static method
        // Can call static method without using class name if used in same class

    }   // instance methods belowng to an instance of a class - use new keyword, can access static amethods and static variables directly - no need  this
    // Make static if not using any instance fields or methods

    // POJOs - Plain old java objects - Mainly just has fields to store data on classes

    // 90. Java Records: Modern POJO approach; write to or read from, db's fields or streams, pass or house data bewtween function
    //      referred to as JavaBeam = asdds stamdards to manipulate objects
    //
}
