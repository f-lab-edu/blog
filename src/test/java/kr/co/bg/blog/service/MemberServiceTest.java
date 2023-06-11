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

        given(memberDAO.create(any())).willReturn(true);
        given(memberDAO.countByUserId(any())).willReturn(0L);

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

        given(memberDAO.create(any())).willReturn(false);
        given(memberDAO.countByUserId(any())).willReturn(0L);

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

        given(memberDAO.countByUserId(any())).willReturn(1L);

        // then
        assertThrows(
                MemberException.class,
                () -> memberService.signUp(
                        givenMember.getUserId(), givenMember.getPassword(), givenMember.getName()));
    }

    @Test
    void 로그인_테스트() {
        // given
        SignInRequest user = SignInRequest.builder()
                .userId("TEST")
                .password("TEST123")
                .build();

        given(passwordEncoder.encode(user.getPassword())).willReturn("encodedPassword");

        Member member = Member.builder()
                .userId(user.getUserId())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        given(memberDAO.findByUserId(user.getUserId())).willReturn(member);
        given(passwordEncoder.matches(user.getPassword(), member.getPassword())).willReturn(true);

        // when
        boolean isLoginSuccess = memberService.signIn(user.getUserId(), user.getPassword());

        // then
        assertThat(isLoginSuccess).isTrue();
    }
}
