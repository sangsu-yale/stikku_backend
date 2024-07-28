package nameco.stikku.security.dto;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginResponseDto {
    private UserInfoDto userInfo;
    private String message;
    private String token;


}
