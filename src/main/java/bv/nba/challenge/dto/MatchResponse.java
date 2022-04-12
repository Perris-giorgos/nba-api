package bv.nba.challenge.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MatchResponse {

    private Integer gameId;
    private LocalDateTime gameDateTime;
    private Set<TeamDetails> teamDetails;
    private List<MatchComment> comments;
    private Set<Scorer> scorers;

}
