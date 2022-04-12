package bv.nba.challenge.dto.freenba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PlayerStats {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("game")
    private Game game;
    @JsonProperty("min")
    private String min;
    @JsonProperty("player")
    private Player player;
    @JsonProperty("pts")
    private Integer pts;
    @JsonProperty("team")
    private Team team;

}
