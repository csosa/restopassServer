package restopass.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.BAD_REQUEST.value(), 1, "Tu usuario o contraseña son incorrectos"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), 2, "El email ya se encuentra registrado en nuestro sistema"),
    ACCESS_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST.value(), 3, "Access token is required for this request"),
    ACCESS_REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST.value(), 4, "Invalid access or refresh token"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), 5, "El email secundario debe ser diferente al principal"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1, "El usuario no esta registrado en RestoPass"),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 2, "Url doesnt exist. Please check our wiki for more info"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 3, "La reserva no existe"),
    USER_NOT_VALID_GOOGLE_LOGIN(HttpStatus.NOT_FOUND.value(), 4, "No se pudo validar el usuario con Google"),

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), 1, "Expired access token"),
    EMPTY_VISITS(HttpStatus.UNAUTHORIZED.value(), 2, "¿Quieres seguir visitando tus restaurantes favoritos? Porque no pruebas un mejor plan"),
    RESERVATION_ALREADY_CONFIRMED(HttpStatus.UNAUTHORIZED.value(), 3, "Reserva ya confirmada"),
    RESERVATION_CANCELED(HttpStatus.UNAUTHORIZED.value(), 4, "Reserva cancelada por el dueño"),
    RESTAURANT_NOT_IN_MEMBERSHIP(HttpStatus.UNAUTHORIZED.value(), 5, "El restaurante no tiene platos disponibles en la membresía de este usuario"),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 0, "Ups, server exploded. Contact Yami");

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
