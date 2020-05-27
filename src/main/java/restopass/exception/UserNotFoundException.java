package restopass.exception;

public class UserNotFoundException extends RestoPassException{

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
