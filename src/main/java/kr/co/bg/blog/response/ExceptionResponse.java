package kr.co.bg.blog.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ExceptionResponse extends ResponseEntity {

    @Builder
    public ExceptionResponse(HttpStatus status, String errorCode, Object data) {
        super(new ExceptionResponseData(status, errorCode, data), status);
    }

    @Getter
    private static class ExceptionResponseData {
        private Integer status;

        private String errorCode;

        private Object data;

        public ExceptionResponseData(HttpStatus status, String errorCode, Object data) {
            this.status = status.value();
            this.errorCode = errorCode;
            this.data = data;
        }
    }
}
