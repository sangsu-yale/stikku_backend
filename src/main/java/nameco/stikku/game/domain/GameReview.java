package nameco.stikku.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game_reviews")
@Getter
@Setter
public class GameReview {
    public GameReview() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_results_id", nullable = false)
    private Long gameResultId;

    private String review;

    private Integer rating;

    @Column(name = "player_of_the_match")
    private String playerOfTheMatch;

    private Mood mood;

    @Column(name = "home_team_lineup")
    private List<String> homeTeamLineup;

    @Column(name = "away_team_lineup")
    private List<String> awayTeamLineup;

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
        ASTONISHED,
        BORED,
        FUN,
    }
}
