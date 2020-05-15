package restopass.exception;

public class InvalidUsernameOrPasswordException extends RestoPassException {
    public InvalidUsernameOrPasswordException() {
        super(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
    }
}
