package restopass.exception;

public class UserAlreadyExistsException extends RestoPassException {

    public UserAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
