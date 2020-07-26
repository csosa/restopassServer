package restopass.exception;

public class ReservationCancelTimeExpiredException extends RestoPassException {
    public ReservationCancelTimeExpiredException() {
        super(ErrorCode.RESERVATION_CANCEL_TIME_EXPIRED);
    }
}
