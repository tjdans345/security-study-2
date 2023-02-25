package dev.study.springscurity2.repository;

import dev.study.springscurity2.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    // 네이밍 쿼리ㅏ 메서드를 이용하여 유저 이메일 검색
    Optional<User> findByEmail(String email);




}
