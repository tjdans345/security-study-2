package dev.study.springscurity2.repository;

import dev.study.springscurity2.domain.entity.User;
import dev.study.springscurity2.domain.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    // """ """ 을 사용해서 칸을 내릴 수 있다 ( 개행 가능 )
    @Query("""
            select t from Token t inner join User u on t.user.id = u.id where u.id = :userId
            and (t.expired = false or t.revoked = false )
               """)
    List<Token> findAllValidTokensByUser(Integer userId);
    // Spring Data Jpa Version
    List<Token> findByUser_IdAndExpiredIsFalseOrRevokedIsFalse(Long userId);

    // 연관관계로 맺어진 엔티티 변수로 접근하는 방법
    List<Token> findAllValidTokensByUserId(Integer userId);
    List<Token> findAllValidTokensByUser(User user);

    Optional<Token> findByToken(String token);



}
