package nameco.stikku.user;

import nameco.stikku.dto.ErrorResponse;
import nameco.stikku.dto.MessageResponse;
import nameco.stikku.dto.UserDTO;
import nameco.stikku.user.exception.InvalidUserDataException;
import nameco.stikku.user.exception.UserNotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO : userService - createUser

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption(userId.toString()));
    }

    public User updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption(userId.toString()));

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
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            // TODO : userService - 게임 삭제 필요
            return userId.toString();
        } else {
            throw new UserNotFoundExeption(userId.toString());
        }
    }

    // TODO : userService - getUserGames

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
