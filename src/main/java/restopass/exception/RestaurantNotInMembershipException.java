package restopass.exception;

public class RestaurantNotInMembershipException extends RestoPassException {
    public RestaurantNotInMembershipException() {
        super(ErrorCode.RESTAURANT_NOT_IN_MEMBERSHIP);
    }
}
