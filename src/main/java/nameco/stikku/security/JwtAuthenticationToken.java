package nameco.stikku.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;

    public JwtAuthenticationToken(String principal, Object credentials, Collection<?> authorities) {
        super(Collections.emptyList());
        this.principal = principal;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return this.principal;
    }
}
