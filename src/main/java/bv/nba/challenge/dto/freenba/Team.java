package bv.nba.challenge.dto.freenba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Team {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("abbreviation")
    private String abbreviation;
    @JsonProperty("city")
    private String city;
    @JsonProperty("conference")
    private String conference;
    @JsonProperty("division")
    private String division;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("name")
    private String name;

}
