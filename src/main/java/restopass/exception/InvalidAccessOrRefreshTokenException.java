package restopass.exception;

public class InvalidAccessOrRefreshTokenException extends RestoPassException {

    public InvalidAccessOrRefreshTokenException() {
        super(ErrorCode.ACCESS_REFRESH_TOKEN_INVALID);
    }
}
