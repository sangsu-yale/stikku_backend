package nameco.stikku.advice.exception;

public class GameResultNotFoundException extends RuntimeException {
    public GameResultNotFoundException() {
        super();
    }

    public GameResultNotFoundException(String userId) {
        super("Game Result not found with id " + userId);
    }
}
