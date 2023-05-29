package kr.co.bg.blog.exception;

public class MemberDuplicatedException extends MemberException {

    public MemberDuplicatedException(MemberErrorCode errorCode) {
        super(errorCode);
    }

    public MemberDuplicatedException(MemberErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
