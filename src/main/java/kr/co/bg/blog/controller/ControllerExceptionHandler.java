package kr.co.bg.blog.controller;

import kr.co.bg.blog.exception.MemberDuplicatedException;
import kr.co.bg.blog.exception.MemberException;
import kr.co.bg.blog.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MemberDuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse memberDuplicatedException(MemberException e) {
        return new ExceptionResponse(e.getErrorCode().getHttpStatus(), e.getMessage());
    }
}
