package com.init_spring_bean_mvn.demo.dataset.api;

import com.init_spring_bean_mvn.demo.dataset.service.DatasetValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@RestControllerAdvice(assignableTypes = DatasetAnalysisController.class)
public class DatasetAnalysisExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetAnalysisExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        List<ApiFieldError> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiFieldError(error.getField(), error.getDefaultMessage()))
                .sorted(Comparator.comparing(ApiFieldError::field))
                .toList();
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed", errors, request);
    }

    @ExceptionHandler(DatasetValidationException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleDatasetValidation(
            DatasetValidationException exception,
            HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", exception.getMessage(), exception.fieldErrors(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleUnreadableBody(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "INVALID_JSON", "Request body could not be parsed", List.of(), request);
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        String correlationId = correlationId(request);
        LOGGER.error("Dataset analysis failed correlation_id={} exception_type={}",
                correlationId, exception.getClass().getName());
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS_ERROR",
                "Dataset analysis could not be completed", List.of(), request);
    }

    private org.springframework.http.ResponseEntity<ApiErrorResponse> response(
            HttpStatus status,
            String code,
            String message,
            List<ApiFieldError> errors,
            HttpServletRequest request) {
        return org.springframework.http.ResponseEntity.status(status).body(new ApiErrorResponse(
                code,
                message,
                correlationId(request),
                errors,
                Instant.now()));
    }

    private String correlationId(HttpServletRequest request) {
        Object value = request.getAttribute(CorrelationIdFilter.ATTRIBUTE);
        return value == null ? "unknown" : value.toString();
    }
}
