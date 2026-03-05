package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

public class POJO {
    @Override // annotation symbol, type of metadata, describe additional info of our code
    // print out current state of object
    // can be used by compiler or other types of pre-processing functions to get more information
    // code will run with or without annotation
    public String toString() {
        return "POJO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", classList='" + classList + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getClassList() {
        return classList;
    }

    public void setClassList(String classList) {
        this.classList = classList;
    }

    public POJO(String id, String name, String dateOfBirth, String classList) {
        // could have type that does this call Record Class to not have to have all of this boilerplate code
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.classList = classList;
    }

    private String id;
    private String name;
    private String dateOfBirth;
    private String classList;

}
