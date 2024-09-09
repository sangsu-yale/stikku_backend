package nameco.stikku.game.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game_reviews")
@Getter
@Setter
@Schema(description = "게임 티켓 중 옵션 정보를 담은 객체")
public class GameReview {
    public GameReview() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "DB에 저장되는 Game Review의 고유 아이디", example = "1004")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "클라이언트에서 게임 티켓을 생성할 때 함께 생성하는 고유 식별 번호", example = "0000-0000-...")
    private String uuid;

    @Column(name = "game_results_id", nullable = false)
    @Schema(description = "Game Result 아이디")
    private Long gameResultId;

    @Schema(description = "경기 리뷰")
    private String review;

    @Schema(description = "별점")
    private Integer rating;

    @Column(name = "player_of_the_match")
    @Schema(description = "경기 수훈선수")
    private String playerOfTheMatch;

    @Schema(description = "감정", example = "HAPPY, SURPRISED, ANGRY, BORED, FUN")
    private Mood mood;

    @Column(name = "home_team_lineup")
    @Schema(description = "홈팀 라인업")
    private List<String> homeTeamLineup;

    @Column(name = "away_team_lineup")
    @Schema(description = "원정팀 라인업")
    private List<String> awayTeamLineup;

    @Schema(description = "음식")
    private String food;

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


    public enum Mood {
        HAPPY,
        SURPRISED,
        ANGRY,
        BORED,
        FUN,
    }
}
