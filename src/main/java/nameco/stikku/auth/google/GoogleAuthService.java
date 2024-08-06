package nameco.stikku.auth.google;


import nameco.stikku.auth.google.dto.GoogleUserInfoDto;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleAuthService {

    private final RestTemplate restTemplate;

    private static final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo?";

    public GoogleAuthService() {
        this.restTemplate = new RestTemplate();
    }

    public GoogleUserInfoDto getUserInfoFromAccessToken(String accessToken) {
        String url = USER_INFO_URI + "access_token="+ accessToken;
        System.out.println("url = " + url);
        GoogleUserInfoDto googleUserInfo = restTemplate.getForObject(url, GoogleUserInfoDto.class);

        return googleUserInfo;
    }

}
