package nameco.stikku.advice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String userId) {
        super("User not found with id " + userId);
    }
}
