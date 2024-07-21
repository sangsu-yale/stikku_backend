package nameco.stikku.game.dto;

import lombok.Getter;
import lombok.Setter;
import nameco.stikku.game.domain.GameResult;
import nameco.stikku.game.domain.GameReview;

@Getter
@Setter
public class GameResponseDto  {

    private GameResult gameResult;
    private GameReview gameReview;

    public GameResponseDto(GameResult gameResult, GameReview gameReview) {
        this.gameResult = gameResult;
        this.gameReview = gameReview;
    }
}
