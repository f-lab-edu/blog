package kr.co.bg.blog.controller;

import static java.util.stream.Collectors.joining;

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
        String errorMsg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ControllerExceptionHandler::errorMessage)
                .collect(joining("\n"));

        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .data(errorMsg)
                .build();
    }

    private static String errorMessage(FieldError fieldError) {
        return String.format("%s : %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}
