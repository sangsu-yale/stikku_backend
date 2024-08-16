package nameco.stikku.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import nameco.stikku.game.domain.GameReview;

import java.util.List;

public class GameReviewDto {
    private String uuid;
    private String review;
    private Integer rating;
    private String playerOfTheMatch;
    private GameReview.Mood mood;
    private List<String> homeTeamLineup;
    private List<String> awayTeamLineup;
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
