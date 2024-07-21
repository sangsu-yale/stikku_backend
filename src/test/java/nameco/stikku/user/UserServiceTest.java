package nameco.stikku.user;

import nameco.stikku.user.dto.UserDTO;
import nameco.stikku.advice.exception.InvalidUserDataException;
import nameco.stikku.advice.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[유저 조회] 유저가 존재하는 경우")
    void getUserById_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testUser");

    }

    @Test
    @DisplayName("[유저 조회] 유저가 존재하지 않는 경우")
    void getUserById_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[유저 갱신] 이름과 이메일 모두 갱신")
    void updateUser_updateUsernameAndEmail() {
        User user = new User();
        user.setId(1L);
        user.setUsername("oldUsername");
        user.setEmail("old@example.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUsername");
        userDTO.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("updatedUsername");
        assertThat(result.getEmail()).isEqualTo("updated@example.com");

    }

    @Test
    @DisplayName("[유저 갱신] 일부만 갱신")
    void updateUser_updateUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("oldUsername");
        user.setEmail("old@example.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("updatedUsername");
        assertThat(result.getEmail()).isEqualTo("old@example.com");

    }

    @Test
    @DisplayName("[유저 갱신] 이름을 공란으로 둔 경우")
    void updateUser_emptyUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("oldUsername");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThatThrownBy(() -> userService.updateUser(1L, userDTO))
                .isInstanceOf(InvalidUserDataException.class)
                .hasMessageMatching(".*empty.*");

    }

    @Test
    @DisplayName("[유저 갱신] 이메일 형식이 맞지 않는 경우")
    void updateUser_wrongEmailFormat() {
        User user = new User();
        user.setId(1L);
        user.setEmail("old@example.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("wrongformat");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThatThrownBy(() -> userService.updateUser(1L, userDTO))
                .isInstanceOf(InvalidUserDataException.class)
                .hasMessageMatching(".*email.*");

    }
    @Test
    @DisplayName("[유저 삭제] 유저가 존재하는 경우 정상 삭제")
    void deleteUser_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        String result = userService.deleteUser(1L);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("1");
    }

    @Test
    @DisplayName("[유저 삭제] 유저가 존재하지 않는 경우")
    void deleteUser_UserDoesNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }
}