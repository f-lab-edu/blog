package kr.co.bg.blog.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bg.blog.config.SecurityConfiguration;
import kr.co.bg.blog.exception.member.MemberErrorCode;
import kr.co.bg.blog.request.SignInRequest;
import kr.co.bg.blog.request.SignUpRequest;
import kr.co.bg.blog.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@Import(SecurityConfiguration.class)
@WebMvcTest(value = MemberController.class)
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void 회원가입_성공_테스트() throws Exception {
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        given(
                memberService.signUp(
                        givenMember.getUserId(),
                        givenMember.getPassword(),
                        givenMember.getName()
                )
        ).willReturn(true);

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 회원가입_실패_테스트() throws Exception {
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        given(
                memberService.signUp(
                        givenMember.getUserId(),
                        givenMember.getPassword(),
                        givenMember.getName()
                )
        ).willReturn(false);

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(MemberErrorCode.REGIST_MEMBER_FAILED.getHttpStatus().value()))
                .andExpect(jsonPath("$.errorCode").value(MemberErrorCode.REGIST_MEMBER_FAILED.getErrorCode()))
                .andExpect(jsonPath("$.data").value(MemberErrorCode.REGIST_MEMBER_FAILED.getMessage()))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "A", "ABCDEFGHIJK"})
    @NullAndEmptySource
    void 회원가입_USER_ID_테스트(String userId) throws Exception {
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId(userId)
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("userId")))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"      ", "      1", "ABCD1", "ABCDEFGHIJKL1"})
    @NullAndEmptySource
    void 회원가입_PASSWORD_테스트(String password) throws Exception {
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password(password)
                .name("TEST_NAME")
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("password")))
                .andDo(print());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "김", "홍길동홍길동홍길동홍길"})
    void 회원가입_NAME_테스트(String name) throws Exception {
        SignUpRequest givenMember = SignUpRequest.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name(name)
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("name")))
                .andDo(print());
    }

    @Test
    void 로그인_성공_테스트() throws Exception {
        // given
        SignInRequest loginUser = SignInRequest.builder()
                .userId("TEST")
                .password("TEST123")
                .build();

        given(memberService.signIn(loginUser.getUserId(), loginUser.getPassword())).willReturn(true);

        String content = objectMapper.writeValueAsString(loginUser);

        // then
        mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andDo(print());
    }

    @Test
    void 로그인_실패_테스트() throws Exception {
        // given
        SignInRequest loginUser = SignInRequest.builder()
                .userId("TEST")
                .password("TEST123")
                .build();

        given(memberService.signIn(loginUser.getUserId(), loginUser.getPassword())).willReturn(false);

        String content = objectMapper.writeValueAsString(loginUser);

        // then
        mockMvc.perform(post("/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.errorCode").value(MemberErrorCode.INVALID_USER.getErrorCode()))
                .andExpect(jsonPath("$.data").value(MemberErrorCode.INVALID_USER.getMessage()))
                .andDo(print());
    }

    @ValueSource(strings = {" ", "A", "ABCDEFGHIJK"})
    @NullAndEmptySource
    @ParameterizedTest
    void 로그인_USER_ID_테스트(String userId) throws Exception {
        SignInRequest givenMember = SignInRequest.builder()
                .userId(userId)
                .password("TEST123")
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-in")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("userId")))
                .andDo(print());
    }

    @ValueSource(strings = {"     1", "      ", "ABCD1", "ABCDEFGHIJKL1"})
    @NullAndEmptySource
    @ParameterizedTest
    void 로그인_PASSWORD_테스트(String password) throws Exception {
        SignInRequest givenMember = SignInRequest.builder()
                .userId("TEST_ID")
                .password(password)
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-in")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("password")))
                .andDo(print());
    }
}
