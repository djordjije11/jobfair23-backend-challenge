package com.nordeus.jobfair.auctionservice.auctionservice.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception ex, WebRequest request){
        ex.printStackTrace();
        return handleExceptionInternal(
                ex,
                "Invalid operation. " + ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }
}
