package kr.co.bg.blog.dao;

import static org.assertj.core.api.Assertions.assertThat;

import kr.co.bg.blog.domain.Member;
import kr.co.bg.blog.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class MemberDAOTest {

    private MemberDAO memberDAO;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        this.memberDAO = new MemberDAO(memberRepository);
    }

    @Test
    void create() {
        // given
        Member givenMember = Member.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        // when
        boolean isCreated = memberDAO.create(givenMember);

        // then
        assertThat(isCreated).isTrue();
    }

    @Test
    void countByUserId() {
        // given
        Member givenMember = Member.builder()
                .userId("TEST_ID")
                .password("TEST123")
                .name("TEST_NAME")
                .build();

        memberDAO.create(givenMember);

        // when
        Long userIdCount = memberDAO.countByUserId(givenMember.getUserId());

        // then
        assertThat(userIdCount).isEqualTo(1L);
    }

    @ValueSource(strings = {"TEST_ID"})
    @ParameterizedTest
    void findByUserId(String userId) {
        // given
        Member givenMember = Member.builder()
                .userId(userId)
                .password("TEST123")
                .name("TEST_NAME")
                .build();
        memberDAO.create(givenMember);

        // when
        Member member = memberDAO.findByUserId(userId);

        // then
        assertThat(member.getUserId()).isEqualTo(userId);
    }
}