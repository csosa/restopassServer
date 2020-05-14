package restopass.exception;

public class RestoPassException extends RuntimeException {

    private Integer code;
    private String message;

    public RestoPassException(ErrorCode error) {
        this.code = error.getValue();
        this.message = error.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public int getHttpStatusCode() {
        return this.code < 1000 ? this.code : Integer.parseInt(String.valueOf(this.code).substring(0, 3));
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                "message=" + message +
                '}';
    }
}
