package com.init_spring_bean_mvn.demo.emsbackend.mapper;

import com.init_spring_bean_mvn.demo.emsbackend.dto.AnalystDto;
import com.init_spring_bean_mvn.demo.emsbackend.entity.Analyst;

public class AnalystMapper {
    public static AnalystDto mapToAnalystDto(Analyst analyst) {

        return new AnalystDto(
            analyst.getId(),
            analyst.getFirstName(),
            analyst.getLastName(),
            analyst.getEmail()
        );



    }
        public static Analyst mapToAnalyst(AnalystDto analystDto) {
            return new Analyst(
                analystDto.getId(),
                analystDto.getFirstName(),
                analystDto.getLastName(),
                analystDto.getEmail()
            );
        }
}
