package gomdoliro.gomdol.Config;

import gomdoliro.gomdol.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authenctication information");
        }
        return authentication.getName();
    }

    public static String getCurrentNickname() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("No authenctication information");
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails) {
            //UserDetails객체에서 Member 객체를 가져옴
            Member member = (Member) authentication.getPrincipal();
            return member.getNickname();
        } else {
            throw new RuntimeException("Principal is not an instance of Member");
        }
    }
}
