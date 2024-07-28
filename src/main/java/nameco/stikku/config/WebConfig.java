package nameco.stikku.config;

import nameco.stikku.resolver.AuthArgumentResolver;
import nameco.stikku.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;

    @Autowired
    public WebConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(jwtService));
    }
}
