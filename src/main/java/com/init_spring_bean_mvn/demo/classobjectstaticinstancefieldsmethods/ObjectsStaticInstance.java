package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

public class ObjectsStaticInstance {
    public static void main(String[] args) {
        /*
        * Class : empty form with placeholders like fields given to Object Creation
        * Object : instance of class with actual values in fields
        * Static : belongs to class and shared by all objects of the class
        *   Template is other word
        * New keyword: used to create object and allocate memory for it - can pass data to it
        * ex String: can use literal or new keyword to create string object - assign reference to memory location
        *   All manuipulation done using local variable name
        *   Class - special data type : fields or attributes on objects: created with static or without
        *       - static - requires static dkeyowrd , value of field is store in special memory location and only in one place -
        *       - instance field - omits static, value of field not allocated until meory allocated when object created
        *           - example: variable name and dot notiation used to access
        *       - methods: static and instance methods
        *
        * */

            // write code here



    }
    public static int sumFirstAndLastDigit(int number) {

        if(number < 0) {
            return -1;
        }

        int lastDigit = number % 10;

        while (number >= 10) {
            number /=10; // divide with 10 until 1 digit left to get first digit
        }

        int firstDigit = number; // for clarity and readability

        return firstDigit + lastDigit;
    }
}
