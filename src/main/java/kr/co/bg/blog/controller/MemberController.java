package kr.co.bg.blog.controller;

import javax.validation.Valid;
import kr.co.bg.blog.dto.MemberDTO;
import kr.co.bg.blog.response.Response;
import kr.co.bg.blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public Response signUp(@Valid @RequestBody MemberDTO memberDTO) {
        memberService.signUp(memberDTO.getUserId(), memberDTO.getPassword(), memberDTO.getName());
        return Response.created();
    }
}
