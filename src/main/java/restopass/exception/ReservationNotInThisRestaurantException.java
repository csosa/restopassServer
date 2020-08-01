package restopass.exception;

public class ReservationNotInThisRestaurantException extends RestoPassException {
    public ReservationNotInThisRestaurantException() {
        super(ErrorCode.RESERVATION_NOT_OF_THIS_RESTAURANT);
    }
}
