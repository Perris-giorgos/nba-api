package bv.nba.challenge.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchesResponse {

    private List<MatchResponse> matches;

}
