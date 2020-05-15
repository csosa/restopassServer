package restopass.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
}
