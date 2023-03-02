package dev.study.springscurity2.service;

import dev.study.springscurity2.domain.dto.request.AuthenticationRequest;
import dev.study.springscurity2.domain.dto.request.RegisterRequest;
import dev.study.springscurity2.domain.dto.resposne.AuthenticationResponse;
import dev.study.springscurity2.domain.entity.User;
import dev.study.springscurity2.domain.entity.token.Token;
import dev.study.springscurity2.enums.Role;
import dev.study.springscurity2.enums.TokenType;
import dev.study.springscurity2.repository.TokenRepository;
import dev.study.springscurity2.repository.UserRepository;
import dev.study.springscurity2.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse register(RegisterRequest registerRequest) {

        var user = User.builder()
                .firstName(registerRequest.getFirstname())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        // 유저 이메일 중복 로직 추가해야 코드가 더 안전하고 깔끔함
        User saveUserEntity = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        saveUserToken(saveUserEntity, jwtToken);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }


    /**
     * 로그인
     * @param authenticationRequest 로그인 정보 객체
     * @return AuthenticationResponse
     */
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUserEmail(), authenticationRequest.getPassword())
        );
        var saveUserEntity = userRepository.findByEmail(authenticationRequest.getUserEmail()).orElseThrow(
        );

        var jwtToken = jwtService.generateToken(saveUserEntity);

        // ----------

        // 새로 발행하는 토큰 이외의 토큰들을 만료시킨다.
        revokeAllUserTokens(saveUserEntity);

        // 새로운 토큰 토큰 테이블에 저장
        saveUserToken(saveUserEntity, jwtToken);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {

        var validTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(token ->
                {
                    token.setExpired(true);
                    token.setRevoked(true);
                }
        );
        tokenRepository.saveAll(validTokens);



    }

    /**
     * Token Save Extracted Method
     * @param user
     * @param jwtToken
     */
    private void saveUserToken(User user, String jwtToken) {
        var tokenEntity = Token
                .builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(tokenEntity);
    }

}
