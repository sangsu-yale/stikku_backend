package nameco.stikku.advice.exception;

public class GameReviewNotFoundException extends RuntimeException {
    public GameReviewNotFoundException() {
        super();
    }

    public GameReviewNotFoundException(String userId) {
        super("Game Review not found with id " + userId);
    }
}
