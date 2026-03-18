package com.init_spring_bean_mvn.demo.emsbackend.service;

import com.init_spring_bean_mvn.demo.emsbackend.dto.AnalystDto;
import com.init_spring_bean_mvn.demo.emsbackend.entity.Analyst;
import com.init_spring_bean_mvn.demo.emsbackend.mapper.AnalystMapper;

import java.util.List;

public interface AnalystService {
    AnalystDto createAnalyst(AnalystDto analystDto);

    AnalystDto getEmployeById(Long analystId);

    List<AnalystDto> getAllAnalysts();

    AnalystDto updateAnalyst(Long analystId, AnalystDto analystDto);
}
