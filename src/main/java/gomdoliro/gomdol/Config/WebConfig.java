package gomdoliro.gomdol.Config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 허용할 도메인 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // 허용할 HTTP 메소드 설정
                .allowedHeaders("*") // 허용할 헤더 설정
                .maxAge(3600); // preflight 요청의 유효시간 설정
    }
}
