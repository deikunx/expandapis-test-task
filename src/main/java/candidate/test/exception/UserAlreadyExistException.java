package candidate.test.exception;

public class UserAlreadyExistException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User with username %s is already exist";

    public UserAlreadyExistException(String username) {
        super(ERROR_MESSAGE.formatted(username));
    }
}
