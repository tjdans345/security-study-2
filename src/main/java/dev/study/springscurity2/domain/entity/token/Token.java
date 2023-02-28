package dev.study.springscurity2.domain.entity.token;

import dev.study.springscurity2.domain.entity.User;
import dev.study.springscurity2.enums.TokenType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    // foreignKey 이름을 설정 가능하구나
    // 아래 코드를 보고 참조하자
    // foreignKeyDefinition 속성을 사용하면 외래 키 제약 조건의 SQL 구문을 직접 지정할 수 있다
    // 이를 사용하여 기본값으로 설정되는 외래 키 제약 조건을 더 세부적으로 조정할 수 있다
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false , foreignKey = @ForeignKey(name = "fk_token_user"))
    private User user;

    @Builder

    public Token(Integer id, String token, TokenType tokenType, boolean expired, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }
}
