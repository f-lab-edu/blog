package kr.co.bg.blog.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private MemberErrorCode errorCode;
    private String message;

    public MemberException(MemberErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public MemberException(MemberErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.message = customMessage;
    }
}
