package bv.nba.challenge.exceptions;

import org.springframework.http.HttpStatus;

public class CommentNotFound extends CustomBaseException{

    public CommentNotFound(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
