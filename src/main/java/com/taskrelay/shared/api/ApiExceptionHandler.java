package com.taskrelay.shared.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

@RestControllerAdvice 
public class ApiExceptionHandler {
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ProblemDetail handleNotFound(java.util.NoSuchElementException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed.");
        var errors = ex.getBindingResult().getFieldErrors().stream().map(error -> Map.of(
            "field", error.getField(),
            "message", error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage()
        )).toList();
        problem.setProperty("errors", errors);
        return problem;
    }

}
