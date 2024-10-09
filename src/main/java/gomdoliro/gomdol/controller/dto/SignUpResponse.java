package gomdoliro.gomdol.controller.dto;

import gomdoliro.gomdol.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpResponse {
    private Long id;
    private String email;
    private String nickname;

    @Builder
    public SignUpResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
