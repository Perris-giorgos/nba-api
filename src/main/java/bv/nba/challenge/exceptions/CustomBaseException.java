package bv.nba.challenge.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomBaseException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CustomBaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}