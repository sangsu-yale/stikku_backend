package nameco.stikku.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_authentication")
@Getter @Setter
public class UserAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private long userId;

    private String provider;

    @Column(name="provider_id")
    private String providerId;
}
