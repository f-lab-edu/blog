package kr.co.bg.blog.service;

import kr.co.bg.blog.dao.MemberDAO;
import kr.co.bg.blog.domain.Member;
import kr.co.bg.blog.exception.MemberDuplicatedException;
import kr.co.bg.blog.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberDAO memberDAO;

    @Transactional
    public void signUp(String userId, String password, String name) {
        Member newMember = Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .build();
        duplicatedCheckByUserId(userId);
        memberDAO.create(newMember);
    }

    private void duplicatedCheckByUserId(String userId) {
        Long userCount = memberDAO.countByUserId(userId);

        if (userCount > 0) {
            throw new MemberDuplicatedException(MemberErrorCode.DUPLICATED_USER_ID);
        }
    }
}
