package kr.co.bg.blog.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {
    private final MemberErrorCode memberError;
    private final String message;

    public MemberException(MemberErrorCode memberError) {
        this(memberError, memberError.getMessage());
    }

    public MemberException(MemberErrorCode memberError, String customMessage) {
        super(customMessage);
        this.memberError = memberError;
        this.message = customMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.memberError.getHttpStatus();
    }

    public String getMemberError() {
        return this.memberError.getErrorCode();
    }
}
