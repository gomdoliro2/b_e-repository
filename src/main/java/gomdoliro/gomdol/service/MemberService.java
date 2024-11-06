package gomdoliro.gomdol.service;

import gomdoliro.gomdol.controller.dto.JwtToken;
import gomdoliro.gomdol.controller.dto.SignUpRequest;
import gomdoliro.gomdol.controller.dto.SignUpResponse;
import gomdoliro.gomdol.domain.Member;
import gomdoliro.gomdol.domain.MemberRepository;
import gomdoliro.gomdol.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtToken signIn(String email, String password) {
        //1. email + password 를 기반으로 인증하는 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticatied 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(email, password);

        //2. 실제 검증, authenticate()메서드를 통해 요청된 Member에 대한 검증 진행
        //authenticate 메서드가 실행될 떄 CustomUserDetailsService 에서 만든 LoadUserByUsername 메서드 실행
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 비밀번호 비교를 별도로 확인하고 싶다면,
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //3. 인증 정보를 기반으로 JWT토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if(memberRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if(signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("이메일을 작성하지 않았습니다.");
        }
        if(signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 작성하지 않았습니다.");
        }
        if(signUpRequest.getNickname() == null || signUpRequest.getNickname().isEmpty()) {
            throw new IllegalArgumentException("이름을 입력하지 않았습니다.");
        }
        if(memberRepository.findByEmail(signUpRequest.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이름입니다.");
        }
        //Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        //Member 객체 생성 및 저장
        Member newMember = new Member(signUpRequest.getEmail(),encodedPassword, signUpRequest.getNickname(),roles);
        Member savedMember = memberRepository.save(newMember);

        // SignUpResponse 반환
        return new SignUpResponse(savedMember);
    }
}
