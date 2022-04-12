package bv.nba.challenge.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Scorer {

    private String playerName;
    private Integer points;

}
