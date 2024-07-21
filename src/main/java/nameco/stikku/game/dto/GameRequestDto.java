package nameco.stikku.game.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class GameRequestDto {

    private GameResultDto gameResult;
    private GameReviewDto gameReview;

    public GameRequestDto(GameResultDto gameResult, GameReviewDto gameReview) {
        this.gameResult = gameResult;
        this.gameReview = gameReview;
    }
}

