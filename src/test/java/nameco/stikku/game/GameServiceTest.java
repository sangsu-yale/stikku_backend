package nameco.stikku.game;

import nameco.stikku.advice.exception.GameResultNotFoundException;
import nameco.stikku.advice.exception.MissingFieldException;
import nameco.stikku.advice.exception.UserNotFoundException;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;
import nameco.stikku.game.dto.*;
import nameco.stikku.user.User;
import nameco.stikku.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class GameServiceTest {

    @Mock
    private GameResultRepository gameResultRepository;

    @Mock
    private GameReviewRepository gameReviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("[게임 생성] 정상적으로 생성")
    void createGame_success() {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        when(userRepository.findById(gameRequestDto.getGameResult().getUserId())).thenReturn(Optional.of(new User()));
        when(gameResultRepository.save(any(GameResult.class))).thenAnswer(invocation -> {
            GameResult gameResult = invocation.getArgument(0);
            return gameResult;
        });

        when(gameReviewRepository.save(any(GameReview.class))).thenAnswer(invocation -> {
            GameReview gameReview = invocation.getArgument(0);
            gameReview.setId(1L);
            return gameReview;
        });

        GameResponseDto game = gameService.createGame(gameRequestDto);

        assertThat(game).isNotNull();
        assertThat(game.getGameResult()).isNotNull();
        assertThat(game.getGameReview()).isNotNull();
        assertThat(game.getGameReview().getGameResultId()).isEqualTo(game.getGameResult().getId());
        assertThat(game.getGameResult().getResult()).isEqualTo(gameRequestDto.getGameResult().getResult());
        assertThat(game.getGameReview().getFood()).isEqualTo(gameRequestDto.getGameReview().getFood());
    }

    @Test
    @DisplayName("[게임 생성] 필드 누락")
    void createGame_missingField() {
        GameRequestDto gameRequestDto = createTestGameRequestDto();
        gameRequestDto.getGameResult().setTeam1(null);
        gameRequestDto.getGameResult().setTeam2(null);

        assertThatThrownBy(() -> gameService.createGame(gameRequestDto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageMatching(".*team1.*team2.*");
    }

    @Test
    @DisplayName("[게임 생성] 사용자가 존재하지 않는 경우")
    void createGame_userNotFound() {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.createGame(gameRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[게임 조회] 성공")
    void getGameById_success() {
        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, gameResult.getId());

        when(gameResultRepository.findById(1L)).thenReturn(Optional.of(gameResult));
        when(gameReviewRepository.findByGameResultId(1L)).thenReturn(Optional.of(gameReview));

        GameResponseDto gameResponseDto = gameService.getGameById(1L);

        assertThat(gameResponseDto).isNotNull();
        assertThat(gameResponseDto.getGameResult()).isSameAs(gameResult);
        assertThat(gameResponseDto.getGameReview()).isSameAs(gameReview);
    }

    @Test
    @DisplayName("[게임 조회] Game Result가 없는 경우")
    void getGameById_gameNotFound() {
        when(gameResultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.getGameById(1L))
                .isInstanceOf(GameResultNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[게임 조회] 특정 유저의 모든 게임 조회")
    void getAllGameByUserId() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        GameResult gameResult1 = createTestGameResult(1L);
        GameResult gameResult2 = createTestGameResult(2L);
        GameReview gameReview1 = createTestGameReview(1L, 1L);

        List<GameResult> gameResults = new ArrayList<>();
        gameResults.add(gameResult1);
        gameResults.add(gameResult2);

        when(gameResultRepository.findGameResultByUserId(1L)).thenReturn(gameResults);
        when(gameReviewRepository.findByGameResultId(1L)).thenReturn(Optional.of(gameReview1));
        when(gameReviewRepository.save(any(GameReview.class))).thenReturn(gameReview1);

        List<GameResponseDto> allGameByUserId = gameService.getAllGameByUserId(1L);

        assertThat(allGameByUserId).isNotNull();
        assertThat(allGameByUserId.size()).isEqualTo(2);
        assertThat(allGameByUserId.get(0).getGameResult()).isEqualTo(gameResult1);
        assertThat(allGameByUserId.get(1).getGameResult()).isEqualTo(gameResult2);
        assertThat(allGameByUserId.get(0).getGameReview()).isEqualTo(gameReview1);

    }

    @Test
    @DisplayName("[게임 수정] 성공")
    void updateGame_success() {
        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, gameResult.getId());

        GameRequestDto gameRequestDto = createTestGameRequestDto();

        when(gameResultRepository.findById(1L)).thenReturn(Optional.of(gameResult));
        when(gameReviewRepository.findByGameResultId(1L)).thenReturn(Optional.of(gameReview));
        when(userRepository.findById(gameRequestDto.getGameResult().getUserId())).thenReturn(Optional.of(new User()));

        GameResponseDto updatedGame = gameService.updateGame(1L, gameRequestDto);

        assertThat(updatedGame).isNotNull();
        assertThat(updatedGame.getGameResult().getScore1()).isEqualTo(gameRequestDto.getGameResult().getScore1());
        assertThat(updatedGame.getGameReview().getRating()).isEqualTo(gameRequestDto.getGameReview().getRating());
    }

    @Test
    @DisplayName("[게임 수정] 사용자가 존재하지 않는 경우")
    void updateGame_UserNotFound() {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        when(userRepository.findById(gameRequestDto.getGameResult().getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.updateGame(1L, gameRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[게임 수정] 게임이 존재하지 않는 경우")
    void updateGame_GameNotFound() {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        when(userRepository.findById(gameRequestDto.getGameResult().getUserId())).thenReturn(Optional.of(new User()));
        when(gameResultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.updateGame(1L, gameRequestDto))
                .isInstanceOf(GameResultNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 즐겨찾기 설정 성공")
    void updateGameFavorite_on_success() {
        GameResult gameResult = createTestGameResult(1L);

        when(gameResultRepository.save(any(GameResult.class))).thenAnswer(invocation -> {
            GameResult gameResult1 = invocation.getArgument(0);
            return gameResult;
        });

        when(gameResultRepository.findById(1L)).thenReturn(Optional.of(gameResult));
        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setIsFavorite(true);

        boolean updatedFavorite = gameService.updateFavorite(1L, favoriteUpdateDto);

        assertThat(updatedFavorite).isTrue();
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 즐겨찾기 해제 성공")
    void updateGameFavorite_off_success() {
        GameResult gameResult = createTestGameResult(1L);
        gameResult.setIsFavorite(true);

        when(gameResultRepository.save(any(GameResult.class))).thenAnswer(invocation -> {
            GameResult gameResult1 = invocation.getArgument(0);
            return gameResult;
        });

        when(gameResultRepository.findById(1L)).thenReturn(Optional.of(gameResult));
        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setIsFavorite(false);

        boolean updatedFavorite = gameService.updateFavorite(1L, favoriteUpdateDto);

        assertThat(updatedFavorite).isFalse();
    }

    @Test
    @DisplayName("[게임 즐겨찾기] 게임이 존재하지 않는 경우")
    void updateGameFavorite_GameNotFound() {

        when(gameResultRepository.findById(1L)).thenReturn(Optional.empty());
        FavoriteUpdateDto favoriteUpdateDto = new FavoriteUpdateDto();
        favoriteUpdateDto.setIsFavorite(true);

        assertThatThrownBy(() -> gameService.updateFavorite(1L, favoriteUpdateDto))
                .isInstanceOf(GameResultNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[게임 삭제] 성공")
    void deleteGame_success() {
        GameResult gameResult = createTestGameResult(1L);
        GameReview gameReview = createTestGameReview(1L, gameResult.getId());

        when(gameResultRepository.findById(1L)).thenReturn(Optional.of(gameResult));
        when(gameReviewRepository.findByGameResultId(1L)).thenReturn(Optional.of(gameReview));

        String deletedGameId = gameService.deleteGame(1L);

        assertThat(deletedGameId).isEqualTo("1");
    }

    @Test
    @DisplayName("[게임 삭제] 게임이 존재하지 않는 경우")
    void deleteGame_GameNotFound() {
        GameRequestDto gameRequestDto = createTestGameRequestDto();

        when(userRepository.findById(gameRequestDto.getGameResult().getUserId())).thenReturn(Optional.of(new User()));
        when(gameResultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.deleteGame(1L))
                .isInstanceOf(GameResultNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
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