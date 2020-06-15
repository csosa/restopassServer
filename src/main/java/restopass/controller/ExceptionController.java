package restopass.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import restopass.exception.ErrorCode;
import restopass.exception.RestoPassException;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(RestoPassException.class)
    public ResponseEntity<RestoPassException> handleApiException(RestoPassException apiException) {
        HttpStatus httpStatus = HttpStatus.valueOf(apiException.getHttpStatusCode());

        if (httpStatus.is4xxClientError()) {
            LOGGER.info(apiException.getMessage(), apiException);
        } else {
            LOGGER.error(apiException.getMessage(), apiException);
        }
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestoPassException> handleApiException(Exception runtimeException) {
        RestoPassException restoPassException = new RestoPassException(ErrorCode.SERVER_ERROR);
        HttpStatus httpStatus = HttpStatus.valueOf(restoPassException.getHttpStatusCode());

        if (httpStatus.is4xxClientError()) {
            LOGGER.info(runtimeException.getMessage(), runtimeException);
        } else {
            LOGGER.error(runtimeException.getMessage(), runtimeException);
        }
        return new ResponseEntity<>(restoPassException, httpStatus);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<RestoPassException> handleNotFound() {
        RestoPassException restoPassException = new RestoPassException(ErrorCode.URL_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.valueOf(restoPassException.getHttpStatusCode());

        LOGGER.info(restoPassException.getMessage(), restoPassException);

        return new ResponseEntity<>(restoPassException, httpStatus);
    }
}
