package bv.nba.challenge.controllers;

import bv.nba.challenge.audit.ActionAudit;
import bv.nba.challenge.dto.MatchResponse;
import bv.nba.challenge.dto.MatchesResponse;
import bv.nba.challenge.enums.ActionEnum;
import bv.nba.challenge.services.MatchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/games")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }


    /**
     * Return Lists all NBA matches for a given date.
     */
    @ActionAudit(action = ActionEnum.RETRIEVE_DATE)
    @GetMapping("/{gameDate}")
    public ResponseEntity<MatchesResponse> getGamesByDate(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate gameDate
    ) {

        return ResponseEntity.ok(matchService.getGamesByDate(gameDate));

    }

    /**
     * Return a specific NBA match for a given id.
     */
    @ActionAudit(action = ActionEnum.RETRIEVE_ID)
    @GetMapping
    public ResponseEntity<MatchResponse> getGamesById(@RequestParam(value = "gameId") Integer gameId) {

        return ResponseEntity.ok(matchService.getGameById(gameId));

    }


}
