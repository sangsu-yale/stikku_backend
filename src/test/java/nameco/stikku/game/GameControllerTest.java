package nameco.stikku.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nameco.stikku.auth.google.JwtService;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;
import nameco.stikku.game.dto.FavoriteUpdateDto;
import nameco.stikku.game.dto.GameRequestDto;
import nameco.stikku.game.dto.GameResultDto;
import nameco.stikku.game.dto.GameReviewDto;
import nameco.stikku.setting.Setting;
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
import org.springframework.test.annotation.DirtiesContext;
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
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameControllerTest {

    private final String TESTUSER_USERNAME = "user";
    private final String TESTUSER_EMAIL = "testuser@gmail.com";

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
    private JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    private String token;

    private User savedUser;

    private Setting savedSetting;

    @BeforeEach
    void setUp(){
        gameResultRepository.deleteAll();
        gameReviewRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setEmail(TESTUSER_EMAIL);
        user.setUsername(TESTUSER_USERNAME);
        this.savedUser = userRepository.save(user);

        this.token = jwtService.generateToken(user);
    }

    @Test
    @DisplayName("[게임 생성] 성공")
    void createGame_success() throws Exception {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        String gameRequestDtoJson = objectMapper.writeValueAsString(gameRequestDto);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestDtoJson)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameResult.id").exists())
                .andExpect(jsonPath("$.gameReview.id").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("[게임 생성] 필드가 누락된 경우")
    void createGame_MissingField() throws Exception {

        GameRequestDto gameRequestDto = new GameRequestDto(new GameResultDto(), new GameReviewDto());
        gameRequestDto.getGameResult().setUserId(savedUser.getId());
        String gameRequestDtoJson = objectMapper.writeValueAsString(gameRequestDto);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestDtoJson)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isBadRequest())
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

        userRepository.deleteAll();

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameRequestDtoJson)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("User not found with id "+savedUser.getId().toString())));
    }

    @Test
    @DisplayName("[게임 조회] 성공")
    void getGameById_success() throws Exception {
        GameResult savedGameResult = gameResultRepository.save(createTestGameResult());
        GameReview savedGameReview = gameReviewRepository.save(createTestGameReview(savedGameResult.getId()));

        mockMvc.perform(get("/games/"+savedGameResult.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameResult.id").value(savedGameResult.getId().toString()))
                .andExpect(jsonPath("$.gameReview.id").value(savedGameReview.getId().toString()))
                .andExpect(jsonPath("$.gameReview.gameResultId").value(savedGameReview.getGameResultId().toString()));
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
        GameResult savedGameResult = gameResultRepository.save(createTestGameResult());
        gameReviewRepository.save(createTestGameReview(savedGameResult.getId()));

        GameRequestDto newGameDto = createTestGameRequestDto();
        String newGameDtoJson = objectMapper.writeValueAsString(newGameDto);

        mockMvc.perform(put("/games/"+savedGameResult.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newGameDtoJson)
                        .header("Authorization", "Bearer "+token))
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
                        .content(newGameDtoJson)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("id 1")));
    }

    @Test
    @DisplayName("[게임 삭제] 성공")
    void deleteGame_success() throws Exception {
        GameResult savedGameResult = gameResultRepository.save(createTestGameResult());
        gameReviewRepository.save(createTestGameReview(savedGameResult.getId()));

        mockMvc.perform(delete("/games/"+savedGameResult.getId().toString())
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Game "+savedGameResult.getId().toString())));
    }

    @Test
    @DisplayName("[게임 삭제] 게임이 존재하지 않는 경우")
    void deleteGame_GameNotFound() throws Exception {
        mockMvc.perform(delete("/games/1")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("id 1")));
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 게임 즐겨찾기 설정(on)")
    void updateGameFavorite_on_success() throws Exception {
        GameResult savedGameResult = gameResultRepository.save(createTestGameResult());
        gameReviewRepository.save(createTestGameReview(savedGameResult.getId()));

        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setIsFavorite(true);

        String favoriteUpdateDtoJson = objectMapper.writeValueAsString(favoriteUpdateDto);

        boolean b = gameService.updateFavorite(savedGameResult.getId(), favoriteUpdateDto);

        mockMvc.perform(put("/games/"+savedGameResult.getId().toString()+"/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(favoriteUpdateDtoJson)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 게임 즐겨찾기 설정(off)")
    void updateGameFavorite_off_success() throws Exception {

        GameResult gameResult = createTestGameResult();
        gameResult.setIsFavorite(true);
        GameResult savedGameResult = gameResultRepository.save(gameResult);

        GameReview gameReview = createTestGameReview(savedGameResult.getId());
        gameReviewRepository.save(gameReview);

        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setIsFavorite(false);

        String favoriteUpdateDtoJson = objectMapper.writeValueAsString(favoriteUpdateDto);

        mockMvc.perform(put("/games/"+savedGameResult.getId().toString()+"/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(favoriteUpdateDtoJson)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isNoContent());
    }

    private GameResult createTestGameResult() {

        GameResult gameResult = new GameResult();
        gameResult.setUserId(savedUser.getId());
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

    private GameReview createTestGameReview(Long gameResultId) {
        GameReview gameReview = new GameReview();
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
        gameResultDto.setUserId(savedUser.getId());
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
