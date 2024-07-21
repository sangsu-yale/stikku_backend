package nameco.stikku.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;
import nameco.stikku.game.dto.FavoriteUpdateDto;
import nameco.stikku.game.dto.GameRequestDto;
import nameco.stikku.game.dto.GameResultDto;
import nameco.stikku.game.dto.GameReviewDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserRepository;
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
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class GameControllerTest {

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

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        gameResultRepository.deleteAll();
        gameReviewRepository.deleteAll();
    }

    @Test
    @DisplayName("[게임 생성] 성공")
    void createGame_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameRequestDto gameRequestDto = createTestGameRequestDto();

        String gameRequestDtoJson = objectMapper.writeValueAsString(gameRequestDto);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameResult.id").exists())
                .andExpect(jsonPath("$.gameReview.id").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("[게임 생성] 필드가 누락된 경우")
    void createGame_MissingField() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameRequestDto gameRequestDto = new GameRequestDto(new GameResultDto(), new GameReviewDto());
        String gameRequestDtoJson = objectMapper.writeValueAsString(gameRequestDto);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("userId")))
                .andExpect(jsonPath("$.message").value(containsString("result")))
                .andExpect(jsonPath("$.message").value(containsString("date")))
                .andExpect(jsonPath("$.message").value(containsString("team1")))
                .andExpect(jsonPath("$.message").value(containsString("team2")))
                .andExpect(jsonPath("$.message").value(containsString("score1")))
                .andExpect(jsonPath("$.message").value(containsString("score2")));
    }

    @Test
    @DisplayName("[게임 생성] 유저가 존재하지 않는 경우")
    void createGame_UserNotFound() throws Exception {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        String gameRequestDtoJson = objectMapper.writeValueAsString(gameRequestDto);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestDtoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("User not found with id 1")));
    }

    @Test
    @DisplayName("[게임 조회] 성공")
    void getGameById_success() throws Exception {
        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, 1L);

        gameResultRepository.save(gameResult);
        gameReviewRepository.save(gameReview);

        mockMvc.perform(get("/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameResult.id").value(1))
                .andExpect(jsonPath("$.gameReview.id").value(1))
                .andExpect(jsonPath("$.gameReview.gameResultId").value(1));
    }

    @Test
    @DisplayName("[게임 조회] 게임이 존재하지 않는 경우")
    void getGameById_GameNotFound() throws Exception {

        mockMvc.perform(get("/games/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("id 1")));
    }

    @Test
    @DisplayName("[게임 수정] 성공")
    void updateGame_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, 1L);

        gameResultRepository.save(gameResult);
        gameReviewRepository.save(gameReview);

        GameRequestDto newGameDto = createTestGameRequestDto();
        String newGameDtoJson = objectMapper.writeValueAsString(newGameDto);

        mockMvc.perform(put("/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newGameDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameReview.rating").value(newGameDto.getGameReview().getRating()));
    }

    @Test
    @DisplayName("[게임 수정] 게임이 존재하지 않는 경우")
    void updateGame_GameNotFound() throws Exception {

        GameRequestDto newGameDto = createTestGameRequestDto();
        String newGameDtoJson = objectMapper.writeValueAsString(newGameDto);

        mockMvc.perform(put("/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newGameDtoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("id 1")));
    }

    @Test
    @DisplayName("[게임 삭제] 성공")
    void deleteGame_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, 1L);

        gameResultRepository.save(gameResult);
        gameReviewRepository.save(gameReview);

        mockMvc.perform(delete("/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Game 1")));
    }

    @Test
    @DisplayName("[게임 삭제] 게임이 존재하지 않는 경우")
    void deleteGame_GameNotFound() throws Exception {
        mockMvc.perform(delete("/games/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("id 1")));
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 게임 즐겨찾기 설정(on)")
    void updateGameFavorite_on_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, 1L);

        gameResultRepository.save(gameResult);
        gameReviewRepository.save(gameReview);

        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setFavorite(true);

        String favoriteUpdateDtoJson = objectMapper.writeValueAsString(favoriteUpdateDto);


        boolean b = gameService.updateFavorite(1L, favoriteUpdateDto);

        mockMvc.perform(patch("/games/1/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(favoriteUpdateDtoJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 게임 즐겨찾기 설정(off)")
    void updateGameFavorite_off_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        GameResult gameResult = createTestGameResult(1L);
        gameResult.setIsFavorite(true);
        GameReview gameReview = createTestGameReview(1L, 1L);

        gameResultRepository.save(gameResult);
        gameReviewRepository.save(gameReview);

        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setFavorite(false);

        String favoriteUpdateDtoJson = objectMapper.writeValueAsString(favoriteUpdateDto);

        mockMvc.perform(patch("/games/1/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(favoriteUpdateDtoJson))
                .andExpect(status().isNoContent());
    }

    private GameResult createTestGameResult(Long id) {

        GameResult gameResult = new GameResult();
        gameResult.setId(id);
        gameResult.setUserId(1L);
        gameResult.setResult(GameResult.GameResultStatus.WIN);
        gameResult.setLiveView(true);
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
