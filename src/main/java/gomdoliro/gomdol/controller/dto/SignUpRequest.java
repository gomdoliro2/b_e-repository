package gomdoliro.gomdol.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
}
