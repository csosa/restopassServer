package restopass.exception;

public class ReservationCannotCancelException extends RestoPassException {
    public ReservationCannotCancelException() {
        super(ErrorCode.RESERVATION_CANNOT_CANCEL);
    }
}
