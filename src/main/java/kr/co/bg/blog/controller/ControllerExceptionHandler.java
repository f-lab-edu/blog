package kr.co.bg.blog.controller;

import kr.co.bg.blog.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationCheckException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMsg =String.format("%s : %s.", fieldError.getField(), fieldError.getDefaultMessage());
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .data(errorMsg)
                .build();
    }
}
