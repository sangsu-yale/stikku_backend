package nameco.stikku.game;

import nameco.stikku.advice.exception.GameResultNotFoundException;
import nameco.stikku.advice.exception.UserNotFoundException;
import nameco.stikku.advice.exception.MissingFieldException;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;
import nameco.stikku.game.dto.*;
import nameco.stikku.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GameService {

    private final GameResultRepository gameResultRepository;
    private final GameReviewRepository gameReviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameService(GameResultRepository gameResultRepository, GameReviewRepository gameReviewRepository, UserRepository userRepository) {
        this.gameResultRepository = gameResultRepository;
        this.gameReviewRepository = gameReviewRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GameResponseDto createGame(GameRequestDto gameRequestDto) {

        GameResultDto gameResultDto = gameRequestDto.getGameResult();
        GameReviewDto gameReviewDto = gameRequestDto.getGameReview();

        // 1. 유효성 검사 - 필수값이 다 들어가 있는지 확인
        validateGameRequestDto(gameRequestDto);

        // 2. 유효성 검사 - 유효한 userId인지 확인
        userRepository.findById(gameResultDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(gameResultDto.getUserId().toString()));

        // 3. game result 저장
        GameResult gameResult = new GameResult();
        updateGameResultFields(gameResult, gameResultDto);
        GameResult savedGameResult = gameResultRepository.save(gameResult);

        // 4. game review 저장
        GameReview gameReview = new GameReview();
        gameReview.setGameResultId(savedGameResult.getId());
        updateGameReviewFields(gameReview, gameReviewDto);
        GameReview savedGameReview = gameReviewRepository.save(gameReview);

        // 5. 응답객체 반환
        return new GameResponseDto(savedGameResult, savedGameReview);
    }

    public GameResponseDto getGameById(Long gameId) {
        GameResult gameResult = gameResultRepository.findById(gameId)
                .orElseThrow(() -> new GameResultNotFoundException(gameId.toString()));

        Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameId);
        GameReview gameReview = new GameReview();
        if (gameReviewOptional.isPresent()) {
            gameReview = gameReviewOptional.get();
        } else {
            gameReview.setGameResultId(gameResult.getId());
            gameReview = gameReviewRepository.save(gameReview);
        }
        return new GameResponseDto(gameResult, gameReview);
    }

    public List<GameResponseDto> getAllGameByUserId(Long userId) {
        List<GameResponseDto> gameResponseDtos = new ArrayList<>();
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));

        List<GameResult> gameResults = gameResultRepository.findGameResultByUserId(userId);
        for(GameResult gameResult : gameResults) {
            Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameResult.getId());
            if (gameReviewOptional.isPresent()){
                gameResponseDtos.add(new GameResponseDto(gameResult, gameReviewOptional.get()));
            } else {
                GameReview gameReview = new GameReview();
                gameReview.setGameResultId(gameResult.getId());
                gameReview = gameReviewRepository.save(gameReview);
                gameResponseDtos.add(new GameResponseDto(gameResult, gameReview));
            }
        }
        return gameResponseDtos;
    }

    @Transactional
    public GameResponseDto updateGame(Long gameId, GameRequestDto gameRequestDto) {
        GameResultDto gameResultDto = gameRequestDto.getGameResult();
        GameReviewDto gameReviewDto = gameRequestDto.getGameReview();

        validateGameRequestDto(gameRequestDto);

        // 1. 유효성 검사 - 유효한 userId인지 확인
        userRepository.findById(gameResultDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(gameResultDto.getUserId().toString()));

        // 2. 유효성 검사 - 유효한 gameId인지 확인
        GameResult gameResult = gameResultRepository.findById(gameId)
                .orElseThrow(() -> new GameResultNotFoundException(gameId.toString()));

        Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameId);
        GameReview gameReview = new GameReview();
        if (gameReviewOptional.isPresent()) {
            gameReview = gameReviewOptional.get();
        } else {
            gameReview = new GameReview();
            gameReview.setGameResultId(gameResult.getId());
        }

        updateGameResultFields(gameResult, gameResultDto);
        updateGameReviewFields(gameReview, gameReviewDto);

        gameResultRepository.save(gameResult);
        gameReviewRepository.save(gameReview);

        return new GameResponseDto(gameResult, gameReview);
    }

    @Transactional
    public boolean updateFavorite(Long gameId, FavoriteUpdateDto favoriteUpdateDto) {
        GameResult gameResult = gameResultRepository.findById(gameId).orElseThrow(() -> new GameResultNotFoundException(gameId.toString()));
        gameResult.setIsFavorite(favoriteUpdateDto.isFavorite());
        GameResult saved = gameResultRepository.save(gameResult);
        return saved.getIsFavorite();
    }
    @Transactional
    public String deleteGame(Long gameId) {
        GameResult gameResult = gameResultRepository.findById(gameId)
                .orElseThrow(() -> new GameResultNotFoundException(gameId.toString()));
        Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameId);

        gameResultRepository.deleteById(gameResult.getId());
        if (gameReviewOptional.isPresent()) {
            gameReviewRepository.deleteById(gameReviewOptional.get().getId());
        }

        return gameId.toString();
    }

    private void updateGameResultFields(GameResult gameResult, GameResultDto gameResultDto) {
        gameResult.setUserId(gameResultDto.getUserId());
        gameResult.setResult(gameResultDto.getResult());
        gameResult.setLiveView(gameResultDto.isLiveView());
        gameResult.setDate(gameResultDto.getDate());
        gameResult.setTeam1(gameResultDto.getTeam1());
        gameResult.setTeam2(gameResultDto.getTeam2());
        gameResult.setScore1(gameResultDto.getScore1());
        gameResult.setScore2(gameResultDto.getScore2());
        gameResult.setTeam1IsMyteam(gameResultDto.isTeam1IsMyTeam());
        gameResult.setTeam2IsMyteam(gameResultDto.isTeam2IsMyTeam());
        gameResult.setComment(gameResultDto.getComment());
        gameResult.setPictureUrl(gameResultDto.getPictureUrl());
    }

    private void updateGameReviewFields(GameReview gameReview, GameReviewDto gameReviewDto) {
        gameReview.setReview(gameReviewDto.getReview());
        gameReview.setRating(gameReviewDto.getRating());
        gameReview.setPlayerOfTheMatch(gameReviewDto.getPlayerOfTheMatch());
        gameReview.setMood(gameReviewDto.getMood());
        gameReview.setHomeTeamLineup(gameReviewDto.getHomeTeamLineup());
        gameReview.setAwayTeamLineup(gameReviewDto.getAwayTeamLineup());
        gameReview.setFood(gameReviewDto.getFood());
    }

    private void validateGameRequestDto(GameRequestDto gameRequestDto) {
        List<String> requiredNullFields = new ArrayList<>();

        if (gameRequestDto.getGameResult().getUserId() == null) requiredNullFields.add("userId");
        if (gameRequestDto.getGameResult().getResult() == null) requiredNullFields.add("result");
        if (gameRequestDto.getGameResult().getDate() == null) requiredNullFields.add("date");
        if (gameRequestDto.getGameResult().getTeam1() == null) requiredNullFields.add("team1");
        if (gameRequestDto.getGameResult().getTeam2() == null) requiredNullFields.add("team2");
        if (gameRequestDto.getGameResult().getScore1() == null) requiredNullFields.add("score1");
        if (gameRequestDto.getGameResult().getScore2() == null) requiredNullFields.add("score2");

        if (!requiredNullFields.isEmpty()) {
            throw new MissingFieldException("Missing required fields: " + requiredNullFields.toString());
        }

    }
}
