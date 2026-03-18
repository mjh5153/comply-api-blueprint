package com.init_spring_bean_mvn.demo.worker;

public class ThisAndSuper {



    private String thisissuper;
    private String thisisthis;

    public ThisAndSuper() {
        this("", "");
    }

    public ThisAndSuper(String thisissuper, String thisisthis) {
        this.thisissuper = thisissuper;
        this.thisisthis = thisisthis;
    }
    // super a: used to access or call parent class members
    // this used to call current class members - both vriables and methods
    // Can use either anywhere except for static elements as a static method - compile errors will occur

    // this - commonly used within getters and optionally in setters

    // super used commonly when method overriding - when call method from parent with same name
    // this() can only be used in constructor and must be first statement - used in constructor chaining - calling constructor fro other constructor
    //      reduces code
    // call to super() must be first statement in each constructor - Can't have both super() and this() in same constructor - compile error will occur
//          make sure last constructor can initialize variables



}
