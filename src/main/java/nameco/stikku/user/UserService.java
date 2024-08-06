package nameco.stikku.user;

import nameco.stikku.auth.google.dto.GoogleUserInfoDto;
import nameco.stikku.game.GameService;
import nameco.stikku.user.dto.UserDTO;
import nameco.stikku.advice.exception.InvalidUserDataException;
import nameco.stikku.advice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GameService gameService;

    @Autowired
    public UserService(UserRepository userRepository, GameService gameService) {
        this.userRepository = userRepository;
        this.gameService = gameService;
    }

    public User findOrCreateUser(GoogleUserInfoDto googleUserInfoDto) {
        return userRepository.findUserByEmail(googleUserInfoDto.getEmail())
                .orElseGet(() -> createUser(new UserDTO(googleUserInfoDto.getName(), googleUserInfoDto.getEmail(), googleUserInfoDto.getPicture())));
    }

    public User createUser(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setProfileImage(userDTO.getProfileImage());
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if(userDTO.getUsername() != null) {
            if (userDTO.getUsername().isEmpty()) {
                throw new InvalidUserDataException("Username can not be empty.");
            }
            user.setUsername(userDTO.getUsername());

        }

        if (userDTO.getEmail() != null) {
            if (!isValidEmail(userDTO.getEmail())) {
                throw new InvalidUserDataException("Invalid email format");
            }
            user.setEmail(userDTO.getEmail());
        }

        if(userDTO.getProfileImage() != null) {
            user.setProfileImage(userDTO.getProfileImage());
        }

        return userRepository.save(user);
    }

    public String deleteUser(Long userId) {
        // TODO : userService - 권한 확인 필요
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            gameService.deleteAllGameByUser(userId);
            return userId.toString();
        } else {
            throw new UserNotFoundException(userId.toString());
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
