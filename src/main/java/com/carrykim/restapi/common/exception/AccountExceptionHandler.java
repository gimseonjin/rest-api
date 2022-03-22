package com.carrykim.restapi.common.exception;

import com.carrykim.restapi.index.controller.IndexController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity usernameNotFoundException(final UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage())
                        .add(linkTo(IndexController.class).withRel("index")));
    }
}
