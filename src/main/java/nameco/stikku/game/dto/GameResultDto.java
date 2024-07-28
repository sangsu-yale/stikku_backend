package nameco.stikku.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import nameco.stikku.game.domain.GameResult;

import java.time.LocalDate;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public GameResult.GameResultStatus getResult() {
        return result;
    }

    public void setResult(GameResult.GameResultStatus result) {
        this.result = result;
    }

    public boolean isLiveView() {
        return isLiveView;
    }

    public void setLiveView(boolean liveView) {
        isLiveView = liveView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getSeatLocation() {
        return seatLocation;
    }

    public void setSeatLocation(String seatLocation) {
        this.seatLocation = seatLocation;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public Integer getScore1() {
        return score1;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    public Integer getScore2() {
        return score2;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }

    public boolean isTeam1IsMyTeam() {
        return team1IsMyTeam;
    }

    public void setTeam1IsMyTeam(boolean team1IsMyTeam) {
        this.team1IsMyTeam = team1IsMyTeam;
    }

    public boolean isTeam2IsMyTeam() {
        return team2IsMyTeam;
    }

    public void setTeam2IsMyTeam(boolean team2IsMyTeam) {
        this.team2IsMyTeam = team2IsMyTeam;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
