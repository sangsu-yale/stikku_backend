package nameco.stikku.advice.exception;

public class MissingFieldException extends RuntimeException {
    public MissingFieldException() {
        super();
    }

    public MissingFieldException(String message) {
        super("Missing required filed : [" + message + "]");
    }
}
