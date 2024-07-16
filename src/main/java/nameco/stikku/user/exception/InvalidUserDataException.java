package nameco.stikku.user.exception;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException() {
        super();
    }

    public InvalidUserDataException(String message) {
        super(message);
    }
}
