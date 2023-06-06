package kr.co.bg.blog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity {

    @Column(unique = true, length = 10)
    private String userId;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Builder
    public Member(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }
}
