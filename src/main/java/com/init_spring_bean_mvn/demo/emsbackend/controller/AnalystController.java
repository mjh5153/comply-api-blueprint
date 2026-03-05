package com.init_spring_bean_mvn.demo.emsbackend.controller;

import com.init_spring_bean_mvn.demo.emsbackend.beans.HelloWorldBean;
import com.init_spring_bean_mvn.demo.emsbackend.dto.AnalystDto;
import com.init_spring_bean_mvn.demo.emsbackend.service.AnalystService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// now able to handle http request

@AllArgsConstructor
@RestController
@RequestMapping("/api/analysts")
public class AnalystController {

    private AnalystService analystService;

    // build add employee REST API
    @GetMapping(path= "/hello-world")
    public HelloWorldBean helloWorld() {

        return new HelloWorldBean("Hello World");
    }
    @PostMapping
    public ResponseEntity<AnalystDto> createAnalyst(@RequestBody AnalystDto analystDto){
        AnalystDto createdAnalyst = analystService.createAnalyst(analystDto);
        return new ResponseEntity<>(createdAnalyst, HttpStatus.CREATED);
    }
    // Can run spring boot application now and hit url from browser or postman to test the api

    @GetMapping("{id}") // need to map mapping to pathVariable to bind uri template varialbe to method parameter
    public ResponseEntity<AnalystDto> getAnalystById(@PathVariable("id") Long analystId) {
        AnalystDto analystDto = analystService.getEmployeById(analystId);
        return new ResponseEntity<>(analystDto, HttpStatus.OK);
    }

    // build get all employees REST API
    @GetMapping()
    public ResponseEntity<List<AnalystDto>> getAllAnalysts() {
        List<AnalystDto> analysts = analystService.getAllAnalysts();
        return ResponseEntity.ok(analysts);
    }

    // Build Update Analyst REST API
    @PutMapping("{id}")
    public ResponseEntity<AnalystDto> updateAnalyst(@PathVariable("id") Long analystId,
                                                    @RequestBody AnalystDto analystDto) { // convert dto to java object, now ready to test with postman client
        AnalystDto updatedAnalyst = analystService.updateAnalyst(analystId, analystDto);
        return ResponseEntity.ok(updatedAnalyst);
    }
}
