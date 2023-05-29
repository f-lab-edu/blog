package kr.co.bg.blog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private Integer status;

    private Object data;

    public static Response ok() {
        return new Response(HttpStatus.OK);
    }

    public static Response created() {
        return new Response(HttpStatus.CREATED);
    }

    private Response(HttpStatus status) {
        this.status = status.value();
    }

    private Response(HttpStatus status, Object data) {
        this.status = status.value();
        this.data = data;
    }
}
