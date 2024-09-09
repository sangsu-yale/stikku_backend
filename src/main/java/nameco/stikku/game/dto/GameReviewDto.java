package nameco.stikku.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import nameco.stikku.game.domain.GameReview;

import java.util.List;

@Schema(description = "게임 티켓 중 옵션 정보를 담은 객체")
public class GameReviewDto {
    @Schema(description = "클라이언트에서 게임 티켓을 생성할 때 함께 생성하는 고유 식별 번호", example = "0000-0000-...")
    private String uuid;
    @Schema(description = "경기 리뷰")
    private String review;
    @Schema(description = "별점")
    private Integer rating;
    @Schema(description = "경기 수훈선수")
    private String playerOfTheMatch;
    @Schema(description = "감정", example = "HAPPY, SURPRISED, ANGRY, BORED, FUN")
    private GameReview.Mood mood;
    @Schema(description = "홈팀 라인업")
    private List<String> homeTeamLineup;
    @Schema(description = "원정팀 라인업")
    private List<String> awayTeamLineup;
    @Schema(description = "음식")
    private String food;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPlayerOfTheMatch() {
        return playerOfTheMatch;
    }

    public void setPlayerOfTheMatch(String playerOfTheMatch) {
        this.playerOfTheMatch = playerOfTheMatch;
    }

    public GameReview.Mood getMood() {
        return mood;
    }

    public void setMood(GameReview.Mood mood) {
        this.mood = mood;
    }

    public List<String> getHomeTeamLineup() {
        return homeTeamLineup;
    }

    public void setHomeTeamLineup(List<String> homeTeamLineup) {
        this.homeTeamLineup = homeTeamLineup;
    }

    public List<String> getAwayTeamLineup() {
        return awayTeamLineup;
    }

    public void setAwayTeamLineup(List<String> awayTeamLineup) {
        this.awayTeamLineup = awayTeamLineup;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
