package kr.co.bg.blog.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum MemberErrorCode {
    REGIST_MEMBER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEC0001", "MEMBER 등록에 실패 했습니다."),
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "MEC0002", "이미 등록된 USER_ID 입니다."),
    INVALID_USER(HttpStatus.UNAUTHORIZED, "MEC0003", "존재 하지 않는 유저이거나, 비밀번호를 확인하세요.");

    private final HttpStatus httpStatus;

    private final String errorCode;

    private final String message;

    MemberErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
