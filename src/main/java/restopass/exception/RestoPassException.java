package restopass.exception;

public class RestoPassException extends RuntimeException {

    private Integer code;

    public RestoPassException(ErrorCode error) {
        super(error.getMessage());
        this.code = error.getValue();
    }

    public int getHttpStatusCode() {
        return this.code < 1000 ? this.code : Integer.parseInt(String.valueOf(this.code).substring(0, 3));
    }

}
