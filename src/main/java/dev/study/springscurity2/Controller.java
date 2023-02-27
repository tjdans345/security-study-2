package dev.study.springscurity2;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class Controller {


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResposne> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        //
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResposne> authenticate(
            @RequestBody AuthenticationRequest registerRequest
    ) {
        //
    }



}
