package bv.nba.challenge.dto;

import bv.nba.challenge.enums.FieldEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamDetails {

    private FieldEnum filed;
    private String teamName;
    private Integer teamScore;


}
