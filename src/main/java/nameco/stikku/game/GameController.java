package nameco.stikku.game;

import nameco.stikku.advice.exception.AccesDeniedException;
import nameco.stikku.game.dto.FavoriteUpdateDto;
import nameco.stikku.game.dto.GameRequestDto;
import nameco.stikku.game.dto.GameResponseDto;
import nameco.stikku.game.dto.GameReviewDto;
import nameco.stikku.resolver.Auth;
import nameco.stikku.responseDto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponseDto> createGame(@Auth String tokenUserId, @RequestBody GameRequestDto gameRequestDto) {
        if(!tokenUserId.equals(gameRequestDto.getGameResult().getUserId().toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }

        return new ResponseEntity<>(gameService.createGame(gameRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<GameResponseDto> getGameById(@PathVariable("uuid") String uuid) {
        return new ResponseEntity<>(gameService.getGameByUuid(uuid), HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<GameResponseDto> updateGame(@Auth String tokenUserId, @PathVariable("uuid") String uuid, @RequestBody GameRequestDto gameRequestDto) {
        if(!tokenUserId.equals(gameRequestDto.getGameResult().getUserId().toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }
        return new ResponseEntity<>(gameService.updateGame(uuid, gameRequestDto), HttpStatus.OK);
    }

    @PutMapping("/{uuid}/favorite")
    public ResponseEntity<Void> updateGameFavorite(@Auth String tokenUserId, @PathVariable("uuid") String uuid, @RequestBody FavoriteUpdateDto favoriteUpdateDto) {
        if (!tokenUserId.equals(gameService.getGameByUuid(uuid).getGameResult().getUserId().toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        gameService.updateFavorite(uuid, favoriteUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<MessageResponse> deleteGame(@Auth String tokenUserId, @PathVariable("uuid") String uuid){
        if (!tokenUserId.equals(gameService.getGameByUuid(uuid).getGameResult().getUserId().toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        String deletedGameId = gameService.deleteGame(uuid);
        return new ResponseEntity<>(new MessageResponse("Game " + deletedGameId + " deleted successfully"), HttpStatus.OK);
    }
}

