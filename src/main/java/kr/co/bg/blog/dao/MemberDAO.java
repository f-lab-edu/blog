package kr.co.bg.blog.dao;

import kr.co.bg.blog.domain.Member;
import kr.co.bg.blog.exception.member.MemberErrorCode;
import kr.co.bg.blog.exception.member.MemberException;
import kr.co.bg.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberDAO {
    private final MemberRepository memberRepository;

    public Long countByUserId(String userId) {
        return memberRepository.countByUserId(userId);
    }

    public Boolean create(Member member) {
        Long count = memberRepository.save(member).getId();
        return count > 0;
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_USER));
    }
}
