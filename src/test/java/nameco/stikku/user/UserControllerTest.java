package nameco.stikku.user;

import nameco.stikku.game.GameResultRepository;
import nameco.stikku.game.GameReviewRepository;
import nameco.stikku.game.GameService;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;
import nameco.stikku.game.dto.GameRequestDto;
import nameco.stikku.game.dto.GameResultDto;
import nameco.stikku.game.dto.GameReviewDto;
import nameco.stikku.user.dto.UserDTO;
import org.assertj.core.api.Assertions;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private GameReviewRepository gameReviewRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        gameResultRepository.deleteAll();
        gameReviewRepository.deleteAll();
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
    @DisplayName("[게임 조회] 특정 유저의 모든 게임 조회")
    void getAllGameByUserId() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameResult gameResult1 = createTestGameResult(1L);
        GameResult gameResult2 = createTestGameResult(2L);
        GameReview gameReview1 = createTestGameReview(1L, 1L);
        GameReview gameReview2 = createTestGameReview(1L, 2L);

        gameResultRepository.save(gameResult1);
        gameResultRepository.save(gameResult2);
        gameReviewRepository.save(gameReview1);
        gameReviewRepository.save(gameReview2);

        mockMvc.perform(get("/users/1/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameResult.id").value(1))
                .andExpect(jsonPath("$[0].gameReview.gameResultId").value(1))
                .andExpect(jsonPath("$", hasSize(2)));

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
        User savedUser = userRepository.save(user);

        GameResult gameResult1 = createTestGameResult(1L);
        gameResult1.setUserId(savedUser.getId());
        GameResult gameResult2 = createTestGameResult(1L);
        gameResult2.setUserId(savedUser.getId());
        GameResult savedGameResult1 = gameResultRepository.save(gameResult1);
        gameResultRepository.save(gameResult2);

        GameReview gameReview1 = createTestGameReview(1L, savedGameResult1.getId());
        gameReviewRepository.save(gameReview1);

        mockMvc.perform(delete("/users/{user_id}", savedUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("User " + user.getId() + " deleted successfully")))
                .andDo(print());

        assertThat(gameService.getAllGameByUserId(savedUser.getId()).size()).isEqualTo(0);
    }

    @Test
    void deleteUser_UserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/users/{user_id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User not found with id 1")))
                .andDo(print());
    }


    private GameResult createTestGameResult(Long id) {

        GameResult gameResult = new GameResult();
        gameResult.setUserId(1L);
        gameResult.setResult(GameResult.GameResultStatus.WIN);
        gameResult.setIsLiveView(true);
        gameResult.setDate(LocalDate.now());
        gameResult.setTeam1("삼성 라이온즈");
        gameResult.setTeam2("기아 타이거즈");
        gameResult.setScore1(2);
        gameResult.setScore2(3);
        gameResult.setTeam1IsMyteam(true);
        gameResult.setComment("개노잼경기 1붐따 드립니다");

        return gameResult;
    }

    private GameReview createTestGameReview(Long id, Long gameResultId) {
        GameReview gameReview = new GameReview();
        gameReview.setId(id);
        gameReview.setGameResultId(gameResultId);
        gameReview.setReview("우리는 언제쯤 기아를 이길수있을까? 기아 엉덩이 만지기도 참으로 지겹다.");
        gameReview.setRating(1);
        gameReview.setPlayerOfTheMatch("이재현");
        gameReview.setMood(GameReview.Mood.BORED);
        gameReview.setFood("두바이초콜릿맛쿠키");

        return gameReview;
    }

    private GameRequestDto createTestGameRequestDto() {

        GameResultDto gameResultDto = new GameResultDto();
        gameResultDto.setUserId(1L);
        gameResultDto.setResult(GameResult.GameResultStatus.WIN);
        gameResultDto.setLiveView(true);
        gameResultDto.setDate(LocalDate.now());
        gameResultDto.setTeam1("삼성 라이온즈");
        gameResultDto.setTeam2("기아 타이거즈");
        gameResultDto.setScore1(5);
        gameResultDto.setScore2(3);
        gameResultDto.setTeam1IsMyTeam(true);
        gameResultDto.setComment("개노잼경기 1붐따 드립니다");

        GameReviewDto gameReviewDto = new GameReviewDto();
        gameReviewDto.setReview("우리는 언제쯤 기아를 이길수있을까? 기아 엉덩이 만지기도 참으로 지겹다.");
        gameReviewDto.setRating(5);
        gameReviewDto.setPlayerOfTheMatch("이재현");
        gameReviewDto.setMood(GameReview.Mood.BORED);
        gameReviewDto.setFood("두바이초콜릿맛쿠키");

        return new GameRequestDto(gameResultDto, gameReviewDto);
    }
}