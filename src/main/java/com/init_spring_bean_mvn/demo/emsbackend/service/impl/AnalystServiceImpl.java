package com.init_spring_bean_mvn.demo.emsbackend.service.impl;

import com.init_spring_bean_mvn.demo.emsbackend.dto.AnalystDto;
import com.init_spring_bean_mvn.demo.emsbackend.entity.Analyst;
import com.init_spring_bean_mvn.demo.emsbackend.exceptions.ResourceNotFoundException;
import com.init_spring_bean_mvn.demo.emsbackend.mapper.AnalystMapper;
import com.init_spring_bean_mvn.demo.emsbackend.repository.AnalystRepository;
import com.init_spring_bean_mvn.demo.emsbackend.service.AnalystService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalystServiceImpl implements AnalystService {

    private AnalystRepository analystRepository;

    @Override
    public AnalystDto createAnalyst(AnalystDto analystDto) {

        // convert employeedto to entity - to store to db
         Analyst analyst = AnalystMapper.mapToAnalyst(analystDto);
         Analyst savedAnalyst = analystRepository.save(analyst);


        return AnalystMapper.mapToAnalystDto(savedAnalyst);
    }

    @Override
    public AnalystDto getEmployeById(Long analystId) { // create custom exception
        Analyst analyst = analystRepository.findById(analystId).orElseThrow(() -> new ResourceNotFoundException("Analyst does not exist " + analystId));
        return AnalystMapper.mapToAnalystDto(analyst);
    }

    @Override
    public List<AnalystDto> getAllAnalysts() {

        List<Analyst> analysts = analystRepository.findAll(); // choose one that has not args
        return analysts.stream().map((analyst) -> AnalystMapper.mapToAnalystDto(analyst)).collect(Collectors.toList()); // map takes lambda -  Function suuplier
    }

    @Override
    public AnalystDto updateAnalyst(Long analystId, AnalystDto updatedAnalyst) {
        Analyst analyst = analystRepository.findById(analystId).orElseThrow(() -> new ResourceNotFoundException("Analyst is not exists with: " + analystId));

        analyst.setFirstName(updatedAnalyst.getFirstName());
        analyst.setLastName(updatedAnalyst.getLastName());
        analyst.setEmail(updatedAnalyst.getEmail());

        Analyst updatedAnalystObj = analystRepository.save(analyst); // insert if analyst repo doesn't have primary already
        return AnalystMapper.mapToAnalystDto(updatedAnalystObj);
    }

    // Test from postman /api/analysts with post method and json body
    // Response = {
    //    "id": 1,
    //    "firstName": "Deadpool",
    //    "lastName": "Hess",
    //    "email": "luxuryelectronicsforcheap@gmail.com"
    //}

    //Next Create Get Analyst REST API by id
}
