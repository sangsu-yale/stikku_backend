package nameco.stikku.auth.google;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nameco.stikku.auth.google.dto.GoogleAuthRequestDto;
import nameco.stikku.auth.google.dto.GoogleUserInfoDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/login/oauth/google")
@Tag(name = "Google OAuth Login", description = "구글 소셜 로그인 후 API 서버 액세스 토큰 발급")
public class GoogleAuthController {

    private final UserService userService;
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;

    @Autowired
    public GoogleAuthController(UserService userService, GoogleAuthService googleAuthService, JwtService jwtService) {
        this.userService = userService;
        this.googleAuthService = googleAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping
    @Operation(summary = "구글 소셜 로그인 후 API 서버용 액세스 토큰 발급", description = "구글 소셜 로그인 후 발급된 구글 액세스 토큰을 통해 API 서버 액세스 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API 서버 액세스 토큰 발급 성공", content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "accessToken", description = "구글 소셜 로그인 후 받은 구글 액세스 토큰")
    public ResponseEntity<GoogleOAuthLoginSuccessResponse> authenticateUser(@RequestBody GoogleAuthRequestDto googleAuthRequestDto) {

        String googleAccessToken = googleAuthRequestDto.getAccessToken();

        GoogleUserInfoDto googleUserInfo = googleAuthService.getUserInfoFromAccessToken(googleAccessToken);

        User user = userService.findOrCreateUser(googleUserInfo);

        String accessToken = jwtService.generateToken(user);

        GoogleOAuthLoginSuccessResponse responseBody = new GoogleOAuthLoginSuccessResponse(accessToken);

        return ResponseEntity.ok(responseBody);
    }

    private class GoogleOAuthLoginSuccessResponse {
        private String accessToken;

        public GoogleOAuthLoginSuccessResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }


}
