package nameco.stikku.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO {
    private String username;
    private String email;
    private String profileImage;

    public UserDTO() {
    }

    public UserDTO(String username, String email, String profileImage) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }
}
