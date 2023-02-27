package dev.study.springscurity2.security;


import dev.study.springscurity2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(String userEmail) {

        return username -> userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Email 이 존재하지 않습니다.") );

    }








}
