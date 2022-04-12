package bv.nba.challenge.services;

import bv.nba.challenge.caching.PutCache;
import bv.nba.challenge.dto.MatchComment;
import bv.nba.challenge.dto.MatchResponse;
import bv.nba.challenge.dto.MatchesResponse;
import bv.nba.challenge.dto.Scorer;
import bv.nba.challenge.dto.TeamDetails;
import bv.nba.challenge.dto.freenba.Game;
import bv.nba.challenge.dto.freenba.PlayerStats;
import bv.nba.challenge.enums.FieldEnum;
import bv.nba.challenge.repositories.MatchDetailsRepository;
import bv.nba.challenge.services.externalapi.FreeNbaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatchServiceImpl implements MatchService {

    private final CommentService commentService;
    private final FreeNbaService freeNbaService;
    private final MatchDetailsRepository matchRepository;

    public MatchServiceImpl(CommentService commentService,
                            FreeNbaService freeNbaService,
                            MatchDetailsRepository matchRepository) {
        this.commentService = commentService;
        this.freeNbaService = freeNbaService;
        this.matchRepository = matchRepository;
    }

    /**
     * retrieves all the games for a specific date.
     *
     * @return return Matcesresponse object
     */
    @Override
    @Transactional(readOnly = true)
    @PutCache(batch = true)
    public MatchesResponse getGamesByDate(LocalDate gamesDate) {

        MatchesResponse matchesResponse = new MatchesResponse();
        List<MatchResponse> listOfMatches = new ArrayList<>();

        // call free nba api to get stats
        List<PlayerStats> playersStats = getFreeNbaApiStats(gamesDate, null);

        // map all game ids to their set of statistics
        var statsPerGameMap = mapGameToPlayerStats(playersStats);

        // transform free nba api response to application response object per game
        for (Entry<Integer, List<PlayerStats>> stats : statsPerGameMap.entrySet()) {
            MatchResponse matchResponse = createMatchResponse(stats.getKey(), stats.getValue());
            // add specific match to list of matches
            listOfMatches.add(matchResponse);
        }

        matchesResponse.setMatches(listOfMatches);
        return matchesResponse;
    }

    /**
     * this method returns a MatchResponse object for the requested gameId.
     *
     * @param gameId id of the requested match.
     */
    @Override
    @Transactional(readOnly = true)
    @PutCache
    public MatchResponse getGameById(Integer gameId) {

        // check cache for available data
        var detailsInCache = getMatchResponse(gameId);
        if (detailsInCache != null) {
            return detailsInCache;
        }

        // call free nba api to get stats
        List<PlayerStats> playersStats = getFreeNbaApiStats(null, gameId);

        return createMatchResponse(gameId, playersStats);
    }

    private MatchResponse createMatchResponse(Integer gameId, List<PlayerStats> statsSet) {
        MatchResponse matchResponse = new MatchResponse();

        matchResponse.setGameId(gameId);
        // collect match scorers
        colectAndSetScorersPerGame(matchResponse, statsSet);
        // set match teams and date info
        createAndSetBasicGameInfo(gameId, matchResponse, statsSet);
        // retrieve and set available comments for game id
        matchResponse.setComments(retrieveGameComments(gameId));

        return matchResponse;
    }

    private Map<Integer, List<PlayerStats>> mapGameToPlayerStats(List<PlayerStats> playersStats) {
        return playersStats.stream()
                           .filter(s -> s.getGame() != null)
                           .collect(
                               Collectors.groupingBy(
                                   playerStats ->
                                       playerStats.getGame().getId(),
                                   Collectors.mapping(stats -> stats, Collectors.toList())));
    }

    private void createAndSetBasicGameInfo(Integer gameId, MatchResponse matchResponse,
                                             List<PlayerStats> statsSet) {
        var samplePlayerStats =
            statsSet.stream()
                    .filter(s ->
                        s.getGame() != null
                        && s.getGame().getHomeTeamScore() != null
                        && s.getGame().getVisitorTeamScore() != null
                        && s.getGame().getDate() != null
                    ).findAny()
                    .orElse(statsSet.iterator().hasNext() ? statsSet.iterator().next() : new PlayerStats());

        if (samplePlayerStats.getGame() != null) {
            matchResponse.setTeamDetails(createTeamDetails(samplePlayerStats.getGame(), statsSet));
            matchResponse.setGameDateTime(samplePlayerStats.getGame().getDate());
        } else {
            log.info("Missing game information for game id " + gameId);
        }
    }

    private void colectAndSetScorersPerGame(MatchResponse matchResponse, List<PlayerStats> statsSet) {
        var scorers =
            statsSet.stream()
                    .filter(stat -> stat.getPts() > 0)
                    .map(player ->
                        Scorer
                            .builder()
                            .playerName(player.getPlayer().getFirstName() + " " + player.getPlayer().getLastName())
                            .points(player.getPts())
                            .build()
                    ).collect(Collectors.toSet());
        matchResponse.setScorers(scorers);
    }

    private List<MatchComment> retrieveGameComments(Integer gameId) {

        return commentService.getGameComments(gameId);

    }

    private Set<TeamDetails> createTeamDetails(Game game, List<PlayerStats> statsSet) {

        var homeTeamName =
            statsSet.stream()
                    .filter(player -> player.getTeam() != null
                                      && game.getHomeTeamId().equals(player.getTeam().getId())
                    ).findAny()
                    .orElseThrow()
                    .getTeam().getName();

        var visitorTeamName =
            statsSet.stream()
                    .filter(player -> player.getTeam() != null
                                      && game.getVisitorTeamId().equals(player.getTeam().getId())
                    ).findAny()
                    .orElseThrow()
                    .getTeam().getName();

        return new HashSet<>(Arrays.asList(
            new TeamDetails(FieldEnum.HOME, homeTeamName, game.getHomeTeamScore()),
            new TeamDetails(FieldEnum.VISITOR, visitorTeamName, game.getVisitorTeamScore()))
        );
    }

    private List<PlayerStats> getFreeNbaApiStats(LocalDate gamesDate, Integer gameId) {

        List<PlayerStats> playersStats = new ArrayList<>();
        Integer nextPage = null;
        do {
            var response = freeNbaService.getApiResponse(gamesDate, gameId, nextPage);
            // add new results into stats list
            playersStats.addAll(response.getData());
            // check if there is another page
            if (response.getMeta() != null) {
                nextPage = response.getMeta().getNextPage();
            }
        } while (nextPage != null);


        return playersStats;
    }

    @Nullable
    private MatchResponse getMatchResponse(Integer gameId) {
        var cacheDetailsOptional = matchRepository.findById(gameId);
        if (cacheDetailsOptional.isPresent()) {
            log.info("Using cached data for game id " + gameId);
            MatchResponse response = new MatchResponse();
            var cacheDetails = cacheDetailsOptional.get();
            response.setGameId(cacheDetails.getGameId());
            response.setGameDateTime(cacheDetails.getGameDateTime());
            response.setComments(retrieveGameComments(gameId));
            response.setTeamDetails(
                new HashSet<>(Arrays.asList(
                    new TeamDetails(FieldEnum.HOME, cacheDetails.getHomeTeamName(), cacheDetails.getHomeTeamScore()),
                    new TeamDetails(FieldEnum.VISITOR, cacheDetails.getVisitorTeamName(),
                        cacheDetails.getVisitorTeamScore()))
            ));
            response.setScorers(
                cacheDetails.getScorers()
                            .stream()
                            .map(matchPlayer ->
                                Scorer.builder()
                                      .playerName(matchPlayer.getPlayerName())
                                      .points(matchPlayer.getPoints())
                                      .build()
                            ).collect(Collectors.toSet()));

            return response;
        }
        return null;
    }


}
