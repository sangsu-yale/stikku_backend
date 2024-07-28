package nameco.stikku.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="game_results")
public class GameResult {
    public GameResult() {

    }

    public GameResult(Long id, Long userId, GameResultStatus result, boolean isLiveView, String title, LocalDate date, String stadium, String seatLocation, String team1, String team2, Integer score1, Integer score2, boolean team1IsMyteam, boolean team2IsMyteam, String comment, String pictureUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.result = result;
        this.isLiveView = isLiveView;
        this.title = title;
        this.date = date;
        this.stadium = stadium;
        this.seatLocation = seatLocation;
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.team1IsMyteam = team1IsMyteam;
        this.team2IsMyteam = team2IsMyteam;
        this.comment = comment;
        this.pictureUrl = pictureUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private GameResultStatus result;

    @Column(name = "is_live_view", nullable = false)
    private boolean isLiveView = false;

    private String title;

    @Column(nullable = false)
    private LocalDate date;

    private String stadium;

    @Column(name = "seat_location")
    private String seatLocation;

    @Column(nullable = false)
    private String team1;

    @Column(nullable = false)
    private String team2;

    @Column(nullable = false)
    private Integer score1;

    @Column(nullable = false)
    private Integer score2;

    @Column(name = "team1_is_myteam")
    private boolean team1IsMyteam = false;

    @Column(name = "team2_is_myteam")
    private boolean team2IsMyteam = false;

    private String comment;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "is_favorite")
    private Boolean isFavorite = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public enum GameResultStatus {
        WIN,
        LOSE,
        TIE,
        RAIN,
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public GameResultStatus getResult() {
        return result;
    }

    public boolean getIsLiveView() {
        return isLiveView;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStadium() {
        return stadium;
    }

    public String getSeatLocation() {
        return seatLocation;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public Integer getScore1() {
        return score1;
    }

    public Integer getScore2() {
        return score2;
    }

    public boolean getIsTeam1IsMyteam() {
        return team1IsMyteam;
    }

    public boolean getIsTeam2IsMyteam() {
        return team2IsMyteam;
    }

    public String getComment() {
        return comment;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setResult(GameResultStatus result) {
        this.result = result;
    }

    public void setIsLiveView(boolean liveView) {
        isLiveView = liveView;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public void setSeatLocation(String seatLocation) {
        this.seatLocation = seatLocation;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }

    public void setTeam1IsMyteam(boolean team1IsMyteam) {
        this.team1IsMyteam = team1IsMyteam;
    }

    public void setTeam2IsMyteam(boolean team2IsMyteam) {
        this.team2IsMyteam = team2IsMyteam;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setIsFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
