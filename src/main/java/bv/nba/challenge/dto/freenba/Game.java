package bv.nba.challenge.dto.freenba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Game {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("date")
    private LocalDateTime date;
    @JsonProperty("home_team_id")
    private Integer homeTeamId;
    @JsonProperty("home_team_score")
    private Integer homeTeamScore;
    @JsonProperty("period")
    private Integer period;
    @JsonProperty("postseason")
    private Boolean postseason;
    @JsonProperty("season")
    private Integer season;
    @JsonProperty("status")
    private String status;
    @JsonProperty("time")
    private String time;
    @JsonProperty("visitor_team_id")
    private Integer visitorTeamId;
    @JsonProperty("visitor_team_score")
    private Integer visitorTeamScore;

}
