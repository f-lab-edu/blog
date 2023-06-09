package kr.co.bg.blog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import kr.co.bg.blog.dao.MemberDAO;
import kr.co.bg.blog.domain.Member;
import kr.co.bg.blog.exception.member.MemberException;
import kr.co.bg.blog.request.SignInRequest;
import kr.co.bg.blog.request.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberDAO memberDAO;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void 회원가입_성공_테스트() {
        // given
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        given(memberDAO.create(any(Member.class))).willReturn(true);
        given(memberDAO.countByUserId(anyString())).willReturn(0L);

        // when
        boolean isSuccess = memberService.signUp(
                givenMember.getUserId(), givenMember.getPassword(), givenMember.getName());

        // then
        assertThat(isSuccess).isTrue();
    }

    @Test
    void 회원가입_실패_테스트() {
        // given
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        given(memberDAO.create(any(Member.class))).willReturn(false);
        given(memberDAO.countByUserId(anyString())).willReturn(0L);

        // when
        boolean isSuccess = memberService.signUp(
                givenMember.getUserId(), givenMember.getPassword(), givenMember.getName());

        // then
        assertThat(isSuccess).isFalse();
    }

    @Test
    void 회원가입_ID_중복_테스트() {
        // given
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        given(memberDAO.countByUserId(anyString())).willReturn(1L);

        // then
        assertThrows(
                MemberException.class,
                () -> memberService.signUp(
                        givenMember.getUserId(), givenMember.getPassword(), givenMember.getName()));
    }

    @Test
    void 로그인_성공_테스트() {
        // given
        SignInRequest user = SignInRequest.builder()
                .userId("TEST")
                .password("TEST123")
                .build();

        Member member = Member.builder()
                .userId(user.getUserId())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        given(memberDAO.findByUserId(anyString())).willReturn(member);

        // when
        boolean isLoginSuccess = memberService.signIn(user.getUserId(), user.getPassword());

        // then
        assertThat(isLoginSuccess).isTrue();
    }

    @Test
    void 로그인_비밀번호_일치하지_않는_실패_테스트() {
        // given
        SignInRequest user = SignInRequest.builder()
                .userId("TEST")
                .password("TEST123")
                .build();

        Member member = Member.builder()
                .userId(user.getUserId())
                .password(passwordEncoder.encode("DIFFERENT_PASSWORD"))
                .build();

        given(memberDAO.findByUserId(anyString())).willReturn(member);

        // when
        boolean isLoginSuccess = memberService.signIn(user.getUserId(), user.getPassword());

        // then
        assertThat(isLoginSuccess).isFalse();
    }

    @Test
    void 로그인_USER_ID가_존재하지_않는_실패_테스트() {
        // given
        given(memberDAO.findByUserId(anyString())).willThrow(MemberException.class);

        // when
        boolean isLoginSuccess = memberService.signIn("TEST", "PASSWORD");

        // then
        assertThat(isLoginSuccess).isFalse();
    }
}
