package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

public record OnlyRecord(String id, String name, String dateOfBirth, String classList) {
// parens are the magic - similar to constructor
    // POJO's rarely looked at or modified: tools that will regenerate the data
    // record: jdk 16 - plain data carrier - has more rules built in than pojo
    // special class with immutability for data and memers - containers - constructors and accessors or getters
    // developers don't have to modify the code


}
