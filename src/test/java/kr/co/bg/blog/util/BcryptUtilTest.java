package kr.co.bg.blog.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BcryptUtilTest {

    private BcryptUtil bcryptUtil;

    @BeforeEach
    public void setUp() {
        this.bcryptUtil = new BcryptUtil(new BCryptPasswordEncoder());
    }

    @Test
    void 비밀번호_암호화_검증_테스트() {
        String rawPassword = "test123";
        String encode = bcryptUtil.encode(rawPassword);
        assertThat(bcryptUtil.matches(rawPassword, encode)).isTrue();
    }
}