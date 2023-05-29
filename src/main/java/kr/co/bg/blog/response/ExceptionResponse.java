package kr.co.bg.blog.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private Integer status;

    private String message;

    public ExceptionResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
