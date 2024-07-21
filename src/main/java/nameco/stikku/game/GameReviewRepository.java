package nameco.stikku.game;

import nameco.stikku.game.domain.GameReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameReviewRepository extends JpaRepository<GameReview, Long> {
    Optional<GameReview> findByGameResultId(Long gameResultId);
}
