package restopass.exception;

public class NoCreditCardException extends RestoPassException {
    public NoCreditCardException() {
        super(ErrorCode.CREDIT_CARD_NOT_FOND);
    }
}
