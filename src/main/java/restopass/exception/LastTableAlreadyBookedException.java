package restopass.exception;

public class LastTableAlreadyBookedException extends RestoPassException {
    public LastTableAlreadyBookedException(){
        super(ErrorCode.LAST_TABLE_ALREADY_BOOKED);
    }
}
