package kr.co.bg.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDTO {

    @NotBlank
    @Size(min = 2, max = 10)
    private String userId;

    @Pattern(regexp = "[a-zA-Z0-9]{6,12}(?!\\s)",
            message = "비밀번호는 공백을 제외한 영어, 숫자 조합의 6~12자리만 가능합니다")
    private String password;

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @Builder
    public MemberDTO(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }
}
