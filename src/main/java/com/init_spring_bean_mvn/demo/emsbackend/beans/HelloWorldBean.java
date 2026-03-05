package com.init_spring_bean_mvn.demo.emsbackend.beans;

public class HelloWorldBean {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "HelloWorldBean{" +
                "message='" + message + '\'' +
                '}';
    }

    private String message;

    public HelloWorldBean(String message) {
        this.message = message;
    }
}
