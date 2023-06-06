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
import kr.co.bg.blog.dto.MemberDTO;
import kr.co.bg.blog.exception.member.MemberErrorCode;
import kr.co.bg.blog.service.MemberService;
import org.junit.jupiter.api.Test;
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
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        given(memberService.signUp(givenMember.getUserId(), givenMember.getPassword(), givenMember.getName())).willReturn(true);

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
        MemberDTO givenMember = MemberDTO.builder()
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

    @Test
    void 회원가입_USER_ID_공백_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId(" ")
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
                .andExpect(jsonPath("$.data").value(containsString("userId")));
    }

    @Test
    void 회원가입_USER_ID_2자_미만_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("A")
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
                .andExpect(jsonPath("$.data").value(containsString("userId")));
    }

    @Test
    void 회원가입_USER_ID_10자_초과_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("ABCDEFGHIJK")
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
                .andExpect(jsonPath("$.data").value(containsString("userId")));
    }

    @Test
    void 회원가입_PASSWORD_공백만_있는_경우_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("      ")
                .name("TEST_NAME")
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("password")));
    }

    @Test
    void 회원가입_PASSWORD_공백과_숫자_조합_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("      1")
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

    @Test
    void 회원가입_PASSWORD_6자_미만_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("ABCD1")
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

    @Test
    void 회원가입_PASSWORD_12자_초과_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("ABCDEFGHIJKL1")
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

    @Test
    void 회원가입_NAME_공백_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name(" ")
                .build();

        String content = objectMapper.writeValueAsString(givenMember);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data").value(containsString("name")));
    }

    @Test
    void 회원가입_NAME_2자_미만_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("김")
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
    void 회원가입_NAME_10자_초과_테스트() throws Exception {
        MemberDTO givenMember = MemberDTO.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("홍길동홍길동홍길동홍길")
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
}