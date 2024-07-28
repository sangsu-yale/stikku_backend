package nameco.stikku.config;

import nameco.stikku.security.JwtAuthenticationFilter;
import nameco.stikku.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity  // Spring Security 활성화
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtService jwtService) {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // authorizeHttpRequests: 특정 URL에 대한 접근 권한 정의
                // "/", "/oauth2/code/google/login", "/oauth2/code/google/callback" 경로에 대해서만 접근 허용하고
                // 나머지 경로에 대해서는 인증 필요
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/",
                                "/error",
                                "/favicon.ico",
                                "/oauth2/authorization/google",
                                "/login/oauth2/code/google/login",
                                "/login/oauth2/code/google/logout",
                                "/login/oauth2/code/google/callback")
                        .permitAll()
                        .anyRequest().authenticated()
                )

                // oauth2Login: 구글 Oauth2.0 로그인 기능 구현
                .oauth2Login(oauth2 -> oauth2

                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/google"))
                        .defaultSuccessUrl("/login/oauth2/code/google/callback")
                )
                // logout: 로그아웃 url과 로그아웃 성공 후 리다이렉션 URL 정의
                .logout(logout -> logout
                        .logoutUrl("/login/oauth2/code/google/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/").permitAll())

                // csrf: CSRF 보호 비활성화
                .csrf(csrf -> csrf
                        .disable()
                );

        // JWT 필터 추가: UsernamePasswordAuthenticationFilter 전에 jwtAuthenticationFilter 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}