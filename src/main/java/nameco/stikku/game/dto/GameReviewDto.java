package nameco.stikku.game.dto;

import lombok.Getter;
import lombok.Setter;
import nameco.stikku.game.domain.GameReview;

import java.util.List;

@Getter
@Setter
public class GameReviewDto {
    private String review;
    private Integer rating;
    private String playerOfTheMatch;
    private GameReview.Mood mood;
    private List<String> homeTeamLineup;
    private List<String> awayTeamLineup;
    private String food;
}
