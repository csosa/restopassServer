package restopass.exception;

public class ReservationNofFoundException extends RestoPassException {

    public ReservationNofFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }

}
