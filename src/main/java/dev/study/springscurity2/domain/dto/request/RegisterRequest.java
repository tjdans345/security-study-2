package dev.study.springscurity2.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;


    @Builder
    public RegisterRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}
