package restopass.exception;

public class AccessTokenRequiredException extends RestoPassException {

    public AccessTokenRequiredException() {
        super(ErrorCode.ACCESS_TOKEN_REQUIRED);
    }
}
