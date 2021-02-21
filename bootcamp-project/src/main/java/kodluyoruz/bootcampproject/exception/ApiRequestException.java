package com.kodluyoruz.bootcampproject.exception;

import org.springframework.http.HttpStatus;

public class ApiRequestException extends RuntimeException{

    HttpStatus status;

    public ApiRequestException(String message) {
        this(message,HttpStatus.BAD_REQUEST);
    }

    public ApiRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
