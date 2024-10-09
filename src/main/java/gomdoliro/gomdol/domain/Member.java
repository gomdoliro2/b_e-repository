package gomdoliro.gomdol.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull(message = "이메일은 필수 사항입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "비밀번호은 필수 사항입니다.")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "이름은 필수 사항입니다.")
    @Column(length = 16, nullable = false, unique = true)
    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Builder
    public Member(String email, String password, String nickname, List<String> roles) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }


}
