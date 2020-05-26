package restopass.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.BAD_REQUEST.value(), 1, "Username or password incorrect"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), 2, "Email already in use"),
    ACCESS_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST.value(), 3, "Access token is required for this request"),
    ACCESS_REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST.value(), 4, "Invalid access or refresh token"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 5, "User not found"),

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), 1, "Expired access token");

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
