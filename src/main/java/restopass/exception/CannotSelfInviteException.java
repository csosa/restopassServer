package restopass.exception;

public class CannotSelfInviteException extends RestoPassException {
    public CannotSelfInviteException() {
        super(ErrorCode.CANNOT_SELF_INVITE_RESERVATION);
    }
}
