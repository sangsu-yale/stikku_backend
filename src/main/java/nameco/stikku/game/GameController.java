package nameco.stikku.game;

import nameco.stikku.game.dto.FavoriteUpdateDto;
import nameco.stikku.game.dto.GameRequestDto;
import nameco.stikku.game.dto.GameResponseDto;
import nameco.stikku.game.dto.GameReviewDto;
import nameco.stikku.responseDto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<GameResponseDto> createGame(@RequestBody GameRequestDto gameRequestDto) {
        return new ResponseEntity<>(gameService.createGame(gameRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getGameById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gameService.getGameById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDto> updateGame(@PathVariable("id") Long id, @RequestBody GameRequestDto gameRequestDto) {
        return new ResponseEntity<>(gameService.updateGame(id, gameRequestDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<Void> updateGameFavorite(@PathVariable("id") Long id, @RequestBody FavoriteUpdateDto favoriteUpdateDto) {
        gameService.updateFavorite(id, favoriteUpdateDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteGame(@PathVariable("id") Long id){
        String deletedGameId = gameService.deleteGame(id);
        return new ResponseEntity<>(new MessageResponse("Game " + deletedGameId + " deleted successfully"), HttpStatus.OK);
    }
}

