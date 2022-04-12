package bv.nba.challenge.dto.freenba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Player {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("height_feet")
    private Integer heightFeet;
    @JsonProperty("height_inches")
    private Integer heightInches;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("position")
    private String position;
    @JsonProperty("team_id")
    private Integer teamId;
    @JsonProperty("weight_pounds")
    private Integer weightPounds;

}
