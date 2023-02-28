package dev.study.springscurity2.service;

import dev.study.springscurity2.domain.dto.request.AuthenticationRequest;
import dev.study.springscurity2.domain.dto.request.RegisterRequest;
import dev.study.springscurity2.domain.dto.resposne.AuthenticationResponse;
import dev.study.springscurity2.domain.entity.User;
import dev.study.springscurity2.enums.Role;
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
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var tokenEntity = Token

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUserEmail(), authenticationRequest.getPassword())
        );

        var user = userRepository.findByEmail(authenticationRequest.getUserEmail()).orElseThrow(

        );

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

}
