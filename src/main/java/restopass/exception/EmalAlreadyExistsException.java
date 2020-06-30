package restopass.exception;

public class EmalAlreadyExistsException extends RestoPassException {

    public EmalAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
