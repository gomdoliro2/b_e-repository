package gomdoliro.gomdol.controller;

import gomdoliro.gomdol.Config.SecurityUtil;
import gomdoliro.gomdol.controller.dto.JwtToken;
import gomdoliro.gomdol.controller.dto.SignInRequest;
import gomdoliro.gomdol.controller.dto.SignUpRequest;
import gomdoliro.gomdol.controller.dto.SignUpResponse;
import gomdoliro.gomdol.domain.Member;
import gomdoliro.gomdol.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInRequest signInRequest) {
        JwtToken jwtToken = memberService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        log.info("request email: {}, password: {}", signInRequest.getEmail(), signInRequest.getPassword());
        log.info("jwtToken accessToken = {}, refresh = {}", jwtToken.getAccessToken(),
                jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse savedMember = memberService.signUp(signUpRequest);
        return ResponseEntity.ok(savedMember);
    }

    @PostMapping("/test")
    public String test() {
        return "성공~";
    }
}
