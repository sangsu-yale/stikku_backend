package nameco.stikku.user;

import nameco.stikku.user.dto.UserDTO;
import nameco.stikku.advice.exception.InvalidUserDataException;
import nameco.stikku.advice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
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
        // TODO : userServie - 권한 확인 필요
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            // TODO : userService - 게임 삭제 필요
            return userId.toString();
        } else {
            throw new UserNotFoundException(userId.toString());
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
