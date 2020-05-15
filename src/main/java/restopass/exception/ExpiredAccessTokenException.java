package restopass.exception;

public class ExpiredAccessTokenException extends RestoPassException{

    public ExpiredAccessTokenException() {
        super(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }

}
