package com.carrykim.restapi.event.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private List<String> message;
    private String error;

    public ErrorResponse(HttpStatus httpStatus , BindingResult bindingResult) {
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = bindingResult.getFieldErrors().stream()
                .map(e -> {
                    String m = e.getField() + " : " + e.getDefaultMessage();
                    return m;
                })
                .collect(Collectors.toList());
    }

}