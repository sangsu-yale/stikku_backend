package nameco.stikku.advice.exception;

public class AccesDeniedException extends RuntimeException {
    public AccesDeniedException() {
        super();
    }

    public AccesDeniedException(String message) {
        super(message);
    }
}
