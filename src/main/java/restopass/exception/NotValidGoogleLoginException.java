package restopass.exception;

public class NotValidGoogleLoginException extends RestoPassException {
    public NotValidGoogleLoginException() {
        super(ErrorCode.USER_NOT_VALID_GOOGLE_LOGIN);
    }
}
