package com.init_spring_bean_mvn.demo.emsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalystDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
// used as response mapper between rest api
// now need to convert to entity
