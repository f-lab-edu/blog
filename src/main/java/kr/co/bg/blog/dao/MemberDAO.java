package kr.co.bg.blog.dao;

import kr.co.bg.blog.domain.Member;
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

    public void create(Member member) {
        memberRepository.save(member);
    }
}
