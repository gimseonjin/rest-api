package com.carrykim.restapi.event.global.exception;

import com.carrykim.restapi.index.controller.IndexController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestControllerAdvice
public class EventExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationErrorException(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST,e.getBindingResult())
                        .add(linkTo(IndexController.class).withRel("index")));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customErrorException(final CustomException e){
        return ResponseEntity
                .status(e.getHttpStatus().value())
                .body(new ErrorResponse(e).add(linkTo(IndexController.class).withRel("index")));
    }

}
