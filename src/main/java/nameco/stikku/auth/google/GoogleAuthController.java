package nameco.stikku.auth.google;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nameco.stikku.auth.google.dto.GoogleAuthRequestDto;
import nameco.stikku.auth.google.dto.GoogleUserInfoDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login/oauth/google")
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
    public ResponseEntity<?> authenticateUser(@RequestBody GoogleAuthRequestDto googleAuthRequestDto) {
        String googleAccessToken = googleAuthRequestDto.getAccessToken();

        GoogleUserInfoDto googleUserInfo = googleAuthService.getUserInfoFromAccessToken(googleAccessToken);

        User user = userService.findOrCreateUser(googleUserInfo);

        String accessToken = jwtService.generateToken(user);

        HashMap<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);

        return ResponseEntity.ok(responseBody);
    }


}
