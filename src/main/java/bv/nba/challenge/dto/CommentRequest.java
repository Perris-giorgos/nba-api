package bv.nba.challenge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    private Integer gameId;
    private String commentMessage;

}
