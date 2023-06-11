package kr.co.bg.blog.controller;

import javax.validation.Valid;
import kr.co.bg.blog.exception.member.MemberErrorCode;
import kr.co.bg.blog.exception.member.MemberException;
import kr.co.bg.blog.request.SignInRequest;
import kr.co.bg.blog.request.SignUpRequest;
import kr.co.bg.blog.response.ExceptionResponse;
import kr.co.bg.blog.response.Response;
import kr.co.bg.blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public Response signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        boolean isSuccess = memberService.signUp(
                signUpRequest.getUserId(),
                signUpRequest.getPassword(),
                signUpRequest.getName()
        );

        if (!isSuccess) {
            throw new MemberException(MemberErrorCode.REGIST_MEMBER_FAILED);
        }

        return Response.builder()
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/sign-in")
    public Response signIn(@Valid @RequestBody SignInRequest signInRequest) {
        boolean isSuccess = memberService.signIn(signInRequest.getUserId(), signInRequest.getPassword());

        if (!isSuccess) {
            throw new MemberException(MemberErrorCode.INVALID_USER);
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .build();
    }

    @ExceptionHandler(MemberException.class)
    public ExceptionResponse memberException(MemberException e) {
        return ExceptionResponse.builder()
                .status(e.getHttpStatus())
                .errorCode(e.getMemberError())
                .data(e.getMessage())
                .build();
    }
}
