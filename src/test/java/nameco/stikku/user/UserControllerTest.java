package nameco.stikku.user;

import nameco.stikku.user.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("[유저 조회] 유저가 존재하는 경우")
    void getUserById_UserExists() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        mockMvc.perform(get("/users/{user_id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andDo(print());
    }

    @Test
    void getUserById_UserDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/{user_id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User not found with id 1")))
                .andDo(print());
    }

    @Test
    void updateUser_UserExists() throws Exception {
        User user = new User();
        user.setUsername("oldUsername");
        user.setEmail("old@example.com");
        userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUsername");
        userDTO.setEmail("updated@example.com");

        mockMvc.perform(put("/users/{user_id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"updatedUsername\", \"email\": \"updated@example.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("updatedUsername"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andDo(print());
    }

    @Test
    void deleteUser_UserExists() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        mockMvc.perform(delete("/users/{user_id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("User " + user.getId() + " deleted successfully")))
                .andDo(print());
    }

    @Test
    void deleteUser_UserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/users/{user_id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User not found with id 1")))
                .andDo(print());
    }
}