package nameco.stikku.game.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="game_results")
@Schema(description = "게임 티켓 중 필수 정보를 담은 객체")
public class GameResult {
    public GameResult() {

    }

    public GameResult(Long id, Long userId, GameResultStatus result, boolean isLiveView, String title, LocalDate date, String stadium, String seatLocation, String team1, String team2, Integer score1, Integer score2, boolean isTeam1IsMyteam, boolean team2IsMyteam, String comment, String pictureUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
    @Schema(description = "DB에 저장되는 Game Result의 고유 아이디", example = "1004")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "클라이언트에서 게임 티켓을 생성할 때 함께 생성하는 고유 식별 번호", example = "0000-0000-...")
    private String uuid;

    @Column(name="user_id", nullable = false)
    @Schema(description = "사용자 아이디", example = "2004")
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "게임 결과", example = "WIN, LOSE, TIE, RAIN")
    private GameResultStatus result;

    @Column(name = "is_live_view", nullable = false)
    @Schema(description = "직관/집관 여부 (직관이면 true)")
    private boolean isLiveView = false;

    @Schema(description = "경기 제목")
    private String title;

    @Column(nullable = false)
    @Schema(description = "경기 일자", example = "yyyy-mm-dd")
    private LocalDate date;

    @Schema(description = "경기장")
    private String stadium;

    @Column(name = "seat_location")
    @Schema(description = "좌석")
    private String seatLocation;

    @Column(nullable = false)
    @Schema(description = "팀1 팀명")
    private String team1;

    @Column(nullable = false)
    @Schema(description = "팀2 팀명")
    private String team2;

    @Column(nullable = false)
    @Schema(description = "팀1 점수")
    private Integer score1;

    @Column(nullable = false)
    @Schema(description = "팀2 점수")
    private Integer score2;

    @Column(name = "team1_is_myteam")
    @Schema(description = "팀1 응원팀 여부 (응원팀이면 true)", defaultValue = "false")
    private boolean team1IsMyteam = false;

    @Column(name = "team2_is_myteam")
    @Schema(description = "팀2 응원팀 여부 (응원팀이면 true)", defaultValue = "false")
    private boolean team2IsMyteam = false;

    @Schema(description = "경기 한줄평")
    private String comment;

    @Column(name = "picture_url")
    @Schema(description = "이미지 URL")
    private String pictureUrl;

    @Column(name = "is_favorite")
    @Schema(description = "티켓 즐겨찾기 여부", defaultValue = "false")
    private Boolean isFavorite = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "티켓 생성 날짜(자동 생성)")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "티켓 마지막 수정 날짜(자동 생성 및 업데이트)")
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

    public String getUuid() {
        return uuid;
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
