package restopass.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.BAD_REQUEST.value(), 1, "Username or password incorrect");

    private Integer status;
    private Integer code;
    private String message;

    ErrorCode(Integer status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Integer getValue() {
        if (this.code != null) {
            return this.status * 100 + this.code;
        }

        return this.status;
    }
}
