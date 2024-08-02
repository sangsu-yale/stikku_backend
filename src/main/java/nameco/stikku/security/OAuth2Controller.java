package nameco.stikku.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nameco.stikku.security.dto.LoginResponseDto;
import nameco.stikku.security.dto.UserAuthenticationDto;
import nameco.stikku.security.dto.UserInfoDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserService;
import nameco.stikku.user.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login/oauth2")
@Slf4j
public class OAuth2Controller {

    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;
    private final JwtService jwtService;

    public OAuth2Controller(UserService userService, UserAuthenticationService userAuthenticationService, JwtService jwtService) {
        this.userService = userService;
        this.userAuthenticationService = userAuthenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/code/google/login")
    public ResponseEntity<Map<String, String>> login(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "이미 로그인한 사용자입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody); // 이미 로그인한 상태
        }
        String redirectUri = "/oauth2/authorization/google";
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUri)).build(); // 구글 OAuth2 로그인 페이지로 리다이렉션
    }


    @GetMapping("/code/google/callback")
    public ResponseEntity<Object> callback(OAuth2AuthenticationToken authentication) {

        try{
            User user;
            UserAuthentication userAuthentication;
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            HttpStatus status;

            OAuth2User userPrincipal = authentication.getPrincipal();
            String oauthEmail = userPrincipal.getAttribute("email");

            if(userService.existsByEmail(oauthEmail)){
                user = userService.getUserByEmail(oauthEmail);
                userAuthentication = userAuthenticationService.getUserAuthenticationByUserId(user.getId());
                loginResponseDto.setMessage("로그인 성공");
                status = HttpStatus.OK;
            } else {
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(userPrincipal.getAttribute("name"));
                userDTO.setEmail(oauthEmail);
                userDTO.setProfileImage(userPrincipal.getAttribute("picture"));
                user = userService.createUser(userDTO);

                UserAuthenticationDto userAuthenticationDto = new UserAuthenticationDto();
                userAuthenticationDto.setUserId(user.getId());
                userAuthenticationDto.setProvider("google");
                userAuthenticationDto.setProviderId(userPrincipal.getAttribute("sub"));
                userAuthentication = userAuthenticationService.createUserAuthentication(userAuthenticationDto);

                loginResponseDto.setMessage("신규 유저 생성");
                status = HttpStatus.CREATED;

            }

            UserInfoDto userInfoDto = new UserInfoDto(user.getUsername(), user.getEmail(), userAuthentication.getProvider(), userAuthentication.getProviderId());
            loginResponseDto.setUserInfo(userInfoDto);
            loginResponseDto.setToken(jwtService.generateToken(user));

            return ResponseEntity.status(status).body(loginResponseDto);
        } catch (Exception e) {
            log.info("error", e);
            HashMap<String, String> errorMessageBody = new HashMap<>();
            errorMessageBody.put("error", "인증에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageBody);
        }
    }

    @GetMapping("/code/google/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 성공");
        return ResponseEntity.ok(responseBody);
    }


}
