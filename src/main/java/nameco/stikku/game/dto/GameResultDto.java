package nameco.stikku.game.dto;

import lombok.Getter;
import lombok.Setter;
import nameco.stikku.game.domain.GameResult;

import java.time.LocalDate;

@Getter
@Setter
public class GameResultDto {
    private Long userId;
    private GameResult.GameResultStatus result;
    private boolean isLiveView;
    private String title;
    private LocalDate date;
    private String stadium;
    private String seatLocation;
    private String team1;
    private String team2;
    private Integer score1;
    private Integer score2;
    private boolean team1IsMyTeam;
    private boolean team2IsMyTeam;
    private String comment;
    private String pictureUrl;
}
