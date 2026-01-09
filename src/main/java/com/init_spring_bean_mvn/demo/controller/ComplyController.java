package com.init_spring_bean_mvn.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @Controller
// @ResponseBody //convert java object to json to httpresponse object

// input - data: hierarchy - Feeds, Trains, fine-tunes, or output by AI model
@RestController
public class ComplyController {

    // HTTP GET
    // http://localhost:8080/comply
    // body - { Feeds, Train, Fine-Tunes, AI output }
    @GetMapping("/comply")// map incoming http get to method
    public String Comply() {
        return "Compliant";
    }

}
