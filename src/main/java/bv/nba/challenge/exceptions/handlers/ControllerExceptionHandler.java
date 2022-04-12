package bv.nba.challenge.exceptions.handlers;

import bv.nba.challenge.dto.exceptions.CustomErrorMessage;
import bv.nba.challenge.exceptions.CustomBaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<CustomErrorMessage> handleExceptions(final CustomBaseException ex) {

        return new ResponseEntity<>(
            new CustomErrorMessage(
                ex.getHttpStatus().getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now().toString()
            ),
            new HttpHeaders(),
            ex.getHttpStatus()
        );
    }


}
