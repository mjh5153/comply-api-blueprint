package com.init_spring_bean_mvn.demo.dataset.service;

import com.init_spring_bean_mvn.demo.dataset.api.ApiFieldError;

import java.util.List;

public class DatasetValidationException extends RuntimeException {

    private final List<ApiFieldError> fieldErrors;

    public DatasetValidationException(String message, List<ApiFieldError> fieldErrors) {
        super(message);
        this.fieldErrors = List.copyOf(fieldErrors);
    }

    public List<ApiFieldError> fieldErrors() {
        return fieldErrors;
    }
}
