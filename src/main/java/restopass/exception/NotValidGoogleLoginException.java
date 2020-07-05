package restopass.exception;

public class NotValidGoogleLoginException extends RestoPassException {
    public NotValidGoogleLoginException() {
        super(ErrorCode.INVALID_USER_GOOGLE_LOGIN);
    }
}
