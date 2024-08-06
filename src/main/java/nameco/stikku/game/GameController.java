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

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getGameById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gameService.getGameById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDto> updateGame(@Auth String tokenUserId, @PathVariable("id") Long id, @RequestBody GameRequestDto gameRequestDto) {
        if(!tokenUserId.equals(gameRequestDto.getGameResult().getUserId().toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }
        return new ResponseEntity<>(gameService.updateGame(id, gameRequestDto), HttpStatus.OK);
    }

    @PutMapping("/{id}/favorite")
    public ResponseEntity<Void> updateGameFavorite(@Auth String tokenUserId, @PathVariable("id") Long id, @RequestBody FavoriteUpdateDto favoriteUpdateDto) {
        if (!tokenUserId.equals(gameService.getGameById(id).getGameResult().getUserId().toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        gameService.updateFavorite(id, favoriteUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteGame(@Auth String tokenUserId, @PathVariable("id") Long id){
        if (!tokenUserId.equals(gameService.getGameById(id).getGameResult().getUserId().toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        String deletedGameId = gameService.deleteGame(id);
        return new ResponseEntity<>(new MessageResponse("Game " + deletedGameId + " deleted successfully"), HttpStatus.OK);
    }
}

