package kr.co.bg.blog.controller;

import javax.validation.Valid;
import kr.co.bg.blog.dto.MemberDTO;
import kr.co.bg.blog.exception.member.MemberException;
import kr.co.bg.blog.response.ExceptionResponse;
import kr.co.bg.blog.response.Response;
import kr.co.bg.blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody MemberDTO memberDTO) {
        try {
            memberService.signUp(memberDTO.getUserId(), memberDTO.getPassword(), memberDTO.getName());
            return Response.builder()
                    .status(HttpStatus.CREATED)
                    .build();
        } catch (MemberException e) {
            return ExceptionResponse.builder()
                    .status(e.getHttpStatus())
                    .errorCode(e.getMemberError())
                    .data(e.getMessage())
                    .build();
        }
    }
}
