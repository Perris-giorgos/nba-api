package bv.nba.challenge.caching;

import bv.nba.challenge.dto.MatchResponse;
import bv.nba.challenge.dto.MatchesResponse;
import bv.nba.challenge.dto.Scorer;
import bv.nba.challenge.dto.TeamDetails;
import bv.nba.challenge.entities.cache.MatchDetails;
import bv.nba.challenge.entities.cache.MatchPlayer;
import bv.nba.challenge.enums.FieldEnum;
import bv.nba.challenge.repositories.MatchDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@Transactional
@AllArgsConstructor
@Slf4j
public class CachingAspect {

    private final MatchDetailsRepository matchRepository;

    /**
     * code to cache MatchResponse into DB.
     */
    @AfterReturning(value = "@annotation(PutCache)", returning = "matchResponse")
    public void cacheAfter(JoinPoint joinPoint, Object matchResponse) {

        try {
            var method = extractMethod(joinPoint);
            var putCache = method.getAnnotation(PutCache.class);
            List<MatchDetails> matchDetailsList = new ArrayList<>();

            if (!putCache.batch()) {
                var response = (MatchResponse) matchResponse;
                var matchEntity = createMatchDetails(response);
                matchDetailsList.add(matchEntity);
            } else {
                var response = (MatchesResponse) matchResponse;
                response.getMatches().forEach(match -> {
                    var matchEntity = createMatchDetails(match);
                    matchDetailsList.add(matchEntity);
                });
            }

            matchRepository.saveAll(matchDetailsList);
        } catch (RuntimeException exception) {
            log.warn("Failed to cache data for {} due to error {} ",
                matchResponse,
                exception.getCause());
        }

    }

    private MatchDetails createMatchDetails(MatchResponse matchResponse) {
        log.info("Caching data for game id " + matchResponse.getGameId());

        MatchDetails matchDetails = new MatchDetails();
        matchDetails.setGameId(matchResponse.getGameId());
        matchDetails.setGameDateTime(matchResponse.getGameDateTime());
        setTeamsDetails(matchResponse, matchDetails);
        matchDetails.setScorers(createAndSetMatchPlayers(matchResponse.getScorers(), matchDetails));
        return matchDetails;
    }

    private Set<MatchPlayer> createAndSetMatchPlayers(Set<Scorer> scorers, MatchDetails matchEntity) {
        return
            scorers.stream()
                   .map(scorer ->
                       MatchPlayer.builder()
                                  .playerName(scorer.getPlayerName())
                                  .points(scorer.getPoints())
                                  .match(matchEntity)
                                  .build()
                   ).collect(Collectors.toSet());
    }

    private void setTeamsDetails(MatchResponse returnValue, MatchDetails matchDetails) {
        for (TeamDetails team : returnValue.getTeamDetails()) {
            if (team.getFiled() == FieldEnum.HOME) {
                matchDetails.setHomeTeamName(team.getTeamName());
                matchDetails.setHomeTeamScore(team.getTeamScore());
            } else {
                matchDetails.setVisitorTeamName(team.getTeamName());
                matchDetails.setVisitorTeamScore(team.getTeamScore());
            }
        }
    }

    private Method extractMethod(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

}
