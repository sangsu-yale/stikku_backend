package nameco.stikku.game;

import nameco.stikku.advice.exception.GameResultNotFoundException;
import nameco.stikku.advice.exception.UserNotFoundException;
import nameco.stikku.advice.exception.MissingFieldException;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;
import nameco.stikku.game.dto.*;
import nameco.stikku.user.User;
import nameco.stikku.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public GameResponseDto getGameByUuid(String uuid) {
        GameResult gameResult = gameResultRepository.findByUuid(uuid)
                .orElseThrow(() -> new GameResultNotFoundException(uuid));

        Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameResult.getId());
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
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return new ArrayList<>();
        }

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
    public GameResponseDto updateGame(String uuid, GameRequestDto gameRequestDto) {
        GameResultDto gameResultDto = gameRequestDto.getGameResult();
        GameReviewDto gameReviewDto = gameRequestDto.getGameReview();

        validateGameRequestDto(gameRequestDto);

        // 1. 유효성 검사 - 유효한 userId인지 확인
        userRepository.findById(gameResultDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(gameResultDto.getUserId().toString()));

        // 2. 유효성 검사 - 유효한 gameId인지 확인
        GameResult gameResult = gameResultRepository.findByUuid(uuid)
                .orElseThrow(() -> new GameResultNotFoundException(uuid));

        Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameResult.getId());
        GameReview gameReview;
        if (gameReviewOptional.isPresent()) {
            gameReview = gameReviewOptional.get();
        } else {
            gameReview = new GameReview();
            gameReview.setGameResultId(gameResult.getId());
        }

        updateGameResultFields(gameResult, gameResultDto);
        updateGameReviewFields(gameReview, gameReviewDto);

        GameResult savedGameResult = gameResultRepository.save(gameResult);
        GameReview savedGameReview = gameReviewRepository.save(gameReview);

        return new GameResponseDto(savedGameResult, savedGameReview);
    }

    @Transactional
    public boolean updateFavorite(String uuid, FavoriteUpdateDto favoriteUpdateDto) {
        GameResult gameResult = gameResultRepository.findByUuid(uuid)
                .orElseThrow(() -> new GameResultNotFoundException(uuid));
        gameResult.setIsFavorite(favoriteUpdateDto.getIsFavorite());
        GameResult saved = gameResultRepository.save(gameResult);
        return saved.getIsFavorite();
    }

    @Transactional
    public String deleteGame(String uuid) {
        GameResult gameResult = gameResultRepository.findByUuid(uuid)
                .orElseThrow(() -> new GameResultNotFoundException(uuid));
        Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameResult.getId());

        gameResultRepository.deleteById(gameResult.getId());
        if (gameReviewOptional.isPresent()) {
            gameReviewRepository.deleteById(gameReviewOptional.get().getId());
        }

        return uuid;
    }

    @Transactional
    public String deleteAllGameByUser(Long userId) {
        List<GameResult> gameResults = gameResultRepository.findGameResultByUserId(userId);
        for(GameResult gameResult : gameResults) {
            Optional<GameReview> gameReviewOptional = gameReviewRepository.findByGameResultId(gameResult.getId());
            if(gameReviewOptional.isPresent()){
                gameReviewRepository.deleteById(gameReviewOptional.get().getId());
            }
            gameResultRepository.deleteById(gameResult.getId());
        }
        return userId.toString();
    }

    @Transactional
    public List<GameResponseDto> syncGame(Long userId, GameSyncRequestDto tickets) {
        List<GameResponseDto> existedTickets = tickets.getExistedTickets();
        List<GameRequestDto> newTickets = tickets.getNewTickets();

        // 1. 클라이언트에는 있고, 서버에는 없는 값 삭제
        List<GameResponseDto> dbTickets = getAllGameByUserId(userId);

        Set<Long> existedTicketIds = existedTickets.stream()
                .map(dto -> dto.getGameResult().getId())
                .collect(Collectors.toSet());

        Set<String> dbTicketIds = dbTickets.stream()
                .map(dto -> dto.getGameResult().getUuid())
                .collect(Collectors.toSet());

        dbTickets.stream()
                .filter(dto -> !existedTicketIds.contains(dto.getGameResult().getId()))
                .collect(Collectors.toList())
                .stream()
                .forEach(result -> deleteGame(result.getGameResult().getUuid()));


        for (GameResponseDto existedTicket : existedTickets) {
            // 2. 해당되는 티켓이 서버에 있는지 확인
            // 2-1. 없으면 -> 새 티켓 생성
            // 2-2. 있으면 -> 업데이트

            String uuid = existedTicket.getGameResult().getUuid();
            GameRequestDto gameRequestDto = new GameRequestDto(convertGameResultToDto(existedTicket.getGameResult()), convertGameReviewToDto(existedTicket.getGameReview()));

            if (dbTicketIds.contains(uuid)) {
                updateGame(uuid, gameRequestDto);
            } else {
                createGame(gameRequestDto);
            }
        }

        // 3. 새 게임 저장
        for(GameRequestDto newTicket : newTickets) {
            createGame(newTicket);
        }

        return getAllGameByUserId(userId);

    }

    private void updateGameResultFields(GameResult gameResult, GameResultDto gameResultDto) {
        gameResult.setUuid(gameResultDto.getUuid());
        gameResult.setUserId(gameResultDto.getUserId());
        gameResult.setResult(gameResultDto.getResult());
        gameResult.setIsLiveView(gameResultDto.isLiveView());
        gameResult.setDate(gameResultDto.getDate());
        gameResult.setTeam1(gameResultDto.getTeam1());
        gameResult.setTeam2(gameResultDto.getTeam2());
        gameResult.setScore1(gameResultDto.getScore1());
        gameResult.setScore2(gameResultDto.getScore2());
        gameResult.setTeam1IsMyteam(gameResultDto.isTeam1IsMyTeam());
        gameResult.setTeam2IsMyteam(gameResultDto.isTeam2IsMyTeam());
        gameResult.setComment(gameResultDto.getComment());
        gameResult.setTitle(gameResultDto.getTitle());
        gameResult.setPictureUrl(gameResultDto.getPictureUrl());
        gameResult.setStadium(gameResultDto.getStadium());
        gameResult.setSeatLocation(gameResultDto.getSeatLocation());

    }

    private void updateGameReviewFields(GameReview gameReview, GameReviewDto gameReviewDto) {
        gameReview.setUuid(gameReviewDto.getUuid());
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

        if (gameRequestDto.getGameResult().getUuid() == null) requiredNullFields.add("uuid");
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

    private static GameResultDto convertGameResultToDto(GameResult gameResult) {
        GameResultDto dto = new GameResultDto();

        dto.setUuid(gameResult.getUuid());
        dto.setUserId(gameResult.getUserId());
        dto.setResult(gameResult.getResult());
        dto.setLiveView(gameResult.getIsLiveView());
        dto.setTitle(gameResult.getTitle());
        dto.setDate(gameResult.getDate());
        dto.setStadium(gameResult.getStadium());
        dto.setSeatLocation(gameResult.getSeatLocation());
        dto.setTeam1(gameResult.getTeam1());
        dto.setTeam2(gameResult.getTeam2());
        dto.setScore1(gameResult.getScore1());
        dto.setScore2(gameResult.getScore2());
        dto.setTeam1IsMyTeam(gameResult.getIsTeam1IsMyteam());
        dto.setTeam2IsMyTeam(gameResult.getIsTeam2IsMyteam());
        dto.setComment(gameResult.getComment());
        dto.setPictureUrl(gameResult.getPictureUrl());

        return dto;
    }

    private static GameReviewDto convertGameReviewToDto(GameReview gameReview) {
        GameReviewDto dto = new GameReviewDto();

        dto.setUuid(gameReview.getUuid());
        dto.setReview(gameReview.getReview());
        dto.setRating(gameReview.getRating());
        dto.setPlayerOfTheMatch(gameReview.getPlayerOfTheMatch());
        dto.setMood(gameReview.getMood());
        dto.setHomeTeamLineup(gameReview.getHomeTeamLineup());
        dto.setAwayTeamLineup(gameReview.getAwayTeamLineup());
        dto.setFood(gameReview.getFood());

        return dto;
    }
}
