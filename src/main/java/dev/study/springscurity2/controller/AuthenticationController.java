package dev.study.springscurity2.controller;


import dev.study.springscurity2.domain.dto.request.AuthenticationRequest;
import dev.study.springscurity2.domain.dto.request.RegisterRequest;
import dev.study.springscurity2.domain.dto.resposne.AuthenticationResponse;
import dev.study.springscurity2.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        //

        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest registerRequest
    ) {
        //

        return ResponseEntity.ok(authenticationService.authenticate(registerRequest));
    }



}
