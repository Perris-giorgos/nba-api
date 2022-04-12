package bv.nba.challenge.services;

import bv.nba.challenge.dto.MatchResponse;
import bv.nba.challenge.dto.MatchesResponse;

import java.time.LocalDate;

public interface MatchService {

    MatchesResponse getGamesByDate(LocalDate gamesDate);

    MatchResponse getGameById(Integer id);
}
