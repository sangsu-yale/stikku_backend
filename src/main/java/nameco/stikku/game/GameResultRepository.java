package nameco.stikku.game;

import nameco.stikku.game.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findGameResultByUserId(Long userId);
}
