package nameco.stikku.security;

import nameco.stikku.advice.exception.UserNotFoundException;
import nameco.stikku.security.dto.UserAuthenticationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    public UserAuthenticationService(UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    public UserAuthentication createUserAuthentication(UserAuthenticationDto userAuthenticationDto){
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUserId(userAuthenticationDto.getUserId());
        userAuthentication.setProvider(userAuthenticationDto.getProvider());
        userAuthentication.setProviderId(userAuthenticationDto.getProviderId());
        UserAuthentication savedUserAuthentication = userAuthenticationRepository.save(userAuthentication);
        return savedUserAuthentication;
    }

    public UserAuthentication getUserAuthenticationByUserId(Long userId) {
        return userAuthenticationRepository.findUserAuthenticationByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }
}
