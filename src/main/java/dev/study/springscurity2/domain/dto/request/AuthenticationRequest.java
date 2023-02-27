package dev.study.springscurity2.domain.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class AuthenticationRequest {

    private String userEmail;

    private String password;

    @Builder
    public AuthenticationRequest(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }
}
