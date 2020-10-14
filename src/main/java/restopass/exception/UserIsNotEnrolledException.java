package restopass.exception;

public class UserIsNotEnrolledException extends RestoPassException {
    public UserIsNotEnrolledException() {
        super(ErrorCode.USER_IS_NOT_ENROLLED);
    }
}
