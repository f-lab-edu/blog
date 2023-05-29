package kr.co.bg.blog.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberDTO {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
