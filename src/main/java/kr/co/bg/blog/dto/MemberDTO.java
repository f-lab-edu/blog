package kr.co.bg.blog.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberDTO {
    @Size(min = 1, max = 10)
    private String userId;

    @Pattern(regexp = "[a-zA-Z1-9]{6,12}",
            message = "비밀번호는 영어와 숫자를 포함한 6~12자리 이내로 입력해주세요")
    private String password;

    @Size(min = 1, max = 10)
    private String name;
}
