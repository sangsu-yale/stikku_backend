package nameco.stikku.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserAuthenticationDto {
    private long userId;

    private String provider;

    private String providerId;
}
