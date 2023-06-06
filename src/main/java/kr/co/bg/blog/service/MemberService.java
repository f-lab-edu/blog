package kr.co.bg.blog.service;

import kr.co.bg.blog.dao.MemberDAO;
import kr.co.bg.blog.domain.Member;
import kr.co.bg.blog.exception.member.MemberErrorCode;
import kr.co.bg.blog.exception.member.MemberException;
import kr.co.bg.blog.util.BcryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberDAO memberDAO;

    private final BcryptUtil bcryptUtil;

    @Transactional
    public boolean signUp(String userId, String password, String name) {
        Member newMember = Member.builder()
                .userId(userId)
                .password(bcryptUtil.encode(password))
                .name(name)
                .build();

        duplicatedCheckByUserId(userId);

        return memberDAO.create(newMember);
    }

    private void duplicatedCheckByUserId(String userId) {
        Long userCount = memberDAO.countByUserId(userId);

        if (userCount > 0) {
            throw new MemberException(MemberErrorCode.DUPLICATED_USER_ID);
        }
    }
}
