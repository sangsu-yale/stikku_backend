package nameco.stikku.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {

    private Long id;
    private String name;
    private String email;
    private String provider;
    private String providerId;

    public UserInfoDto(Long id, String name, String email, String provider, String providerId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }
}