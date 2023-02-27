package dev.study.springscurity2.domain.dto.resposne;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;

    @Builder
    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
