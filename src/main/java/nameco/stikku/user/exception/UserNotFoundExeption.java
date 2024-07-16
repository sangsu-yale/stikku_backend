package nameco.stikku.user.exception;

public class UserNotFoundExeption extends RuntimeException {
    public UserNotFoundExeption() {
        super();
    }

    public UserNotFoundExeption(String userId) {
        super("User not found with id " + userId);
    }
}
