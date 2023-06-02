package kr.co.bg.blog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class Response extends ResponseEntity {
    @Builder
    public Response(Object body, HttpStatus status) {
        super(new ResponseData(body, status), status);
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ResponseData {
        private Integer status;

        private Object data;

        public ResponseData(Object data, HttpStatus status) {
            this.status = status.value();
            this.data = data;
        }
    }
}
