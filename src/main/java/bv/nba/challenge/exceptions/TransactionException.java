package bv.nba.challenge.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionException extends CustomBaseException {

    public TransactionException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
