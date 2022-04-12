package bv.nba.challenge.exceptions;

import org.springframework.http.HttpStatus;

public class ExternalApiException extends CustomBaseException {

    public ExternalApiException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
