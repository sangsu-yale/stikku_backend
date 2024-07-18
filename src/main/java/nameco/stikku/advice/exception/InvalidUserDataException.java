package nameco.stikku.advice.exception;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException() {
        super();
    }

    public InvalidUserDataException(String message) {
        super(message);
    }
}
