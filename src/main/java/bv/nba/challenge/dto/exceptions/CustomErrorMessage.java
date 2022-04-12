package bv.nba.challenge.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomErrorMessage {

    private String message;
    private String description;
    private String timestamp;

}
