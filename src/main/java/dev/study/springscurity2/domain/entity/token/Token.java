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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Token(Integer id, String token, TokenType tokenType) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
    }
}
