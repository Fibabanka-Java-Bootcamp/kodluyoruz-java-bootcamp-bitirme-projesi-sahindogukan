package com.kodluyoruz.bootcampproject.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException2 apiException = new ApiException2(
                e.getMessage(),
                e.getStatus(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleNotFoundException(NotFoundException ex, HttpServletRequest request){
        ApiException error = new ApiException(404, ex.getMessage(), request.getServletPath());
        return error;
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleJsonParseException(JsonParseException ex, HttpServletRequest request){
        ApiException error = new ApiException(400, "Bu alan sayısal bir veri içermelidir.", request.getServletPath());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleMethodArgNotValid(MethodArgumentNotValidException exception, HttpServletRequest request){
        ApiException error = new ApiException(400);
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, String> validationErrors = new HashMap<>();
        for(FieldError fieldError: bindingResult.getFieldErrors()){
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setValidationErrors(validationErrors);
        return error;
    }

}

