package nameco.stikku.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import nameco.stikku.game.domain.GameResult;

import java.time.LocalDate;

@Schema(description = "게임 티켓 중 필수 정보를 담은 객체")
public class GameResultDto {
    @Schema(description = "사용자 아이디", example = "2004")
    private Long userId;
    @Schema(description = "클라이언트에서 게임 티켓을 생성할 때 함께 생성하는 고유 식별 번호", example = "0000-0000-...")
    private String uuid;
    @Schema(description = "게임 결과", example = "WIN, LOSE, TIE, RAIN")
    private GameResult.GameResultStatus result;
    @Schema(description = "직관/집관 여부 (직관이면 true)")
    private boolean isLiveView;
    @Schema(description = "경기 제목")
    private String title;
    @Schema(description = "경기 일자", example = "yyyy-mm-dd")
    private LocalDate date;
    @Schema(description = "경기장")
    private String stadium;
    @Schema(description = "좌석")
    private String seatLocation;
    @Schema(description = "팀1 팀명")
    private String team1;
    @Schema(description = "팀2 팀명")
    private String team2;
    @Schema(description = "팀1 점수")
    private Integer score1;
    @Schema(description = "팀2 점수")
    private Integer score2;
    @Schema(description = "팀1 응원팀 여부 (응원팀이면 true)", defaultValue = "false")
    private boolean team1IsMyTeam;
    @Schema(description = "팀2 응원팀 여부 (응원팀이면 true)", defaultValue = "false")
    private boolean team2IsMyTeam;
    @Schema(description = "경기 한줄평")
    private String comment;
    @Schema(description = "이미지 URL")
    private String pictureUrl;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
