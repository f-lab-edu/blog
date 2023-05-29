package kr.co.bg.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum MemberErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 등록된 USER_ID 입니다.");

    private HttpStatus httpStatus;

    private String message;

    MemberErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
