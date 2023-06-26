package kr.co.bg.blog.repository;

import java.util.Optional;
import kr.co.bg.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Long countByUserId(String userId);

    Optional<Member> findByUserId(String userId);
}
