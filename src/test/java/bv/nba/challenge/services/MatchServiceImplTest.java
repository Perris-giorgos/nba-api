package bv.nba.challenge.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bv.nba.challenge.dto.freenba.StatsResponse;
import bv.nba.challenge.entities.cache.MatchDetails;
import bv.nba.challenge.exceptions.ExternalApiException;
import bv.nba.challenge.exceptions.TransactionException;
import bv.nba.challenge.repositories.MatchDetailsRepository;
import bv.nba.challenge.services.externalapi.FreeNbaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @InjectMocks
    MatchServiceImpl service;
    @Mock
    CommentServiceImpl commentService;
    @Mock
    MatchDetailsRepository matchDetailsRepository;
    @Mock
    FreeNbaService nbaService;

    private ObjectMapper mapper = new ObjectMapper();
    private static final String relPath = System.getProperty("user.dir");
    private StatsResponse responseDates = mapper.readValue(
        new File(relPath + "/src/test/resources/nba_api_stats_response.json"), StatsResponse.class);

    MatchServiceImplTest() throws IOException {}


    @Test
    void getGamesByDate() {
        when(nbaService.getApiResponse(any(), any(), any())).thenReturn(responseDates);

        var response = service.getGamesByDate(LocalDate.now());

        verify(nbaService).getApiResponse(any(), any(), any());
        assertFalse(response.getMatches().isEmpty());
        assertEquals(1, response.getMatches().size());

    }

    @Test
    void getGamesByDateApiFailed() {
        when(nbaService.getApiResponse(any(), any(), any())).thenThrow(ExternalApiException.class);

        assertThrows(ExternalApiException.class, () -> service.getGamesByDate(LocalDate.of(2020, 1, 8)));

    }


    @Test
    void getGameById() {
        when(nbaService.getApiResponse(any(), any(), any())).thenReturn(responseDates);

        when(matchDetailsRepository.findById(2)).thenReturn(Optional.empty());

        var response = service.getGameById(2);

        verify(nbaService).getApiResponse(any(), any(), any());
        assertNotNull(response);
        assertEquals(2, response.getGameId());

    }

    @Test
    void getGameByIdByCache() {

        MatchDetails matchDetails = new MatchDetails();
        matchDetails.setGameId(2);
        matchDetails.setVisitorTeamScore(100);
        matchDetails.setHomeTeamScore(99);
        matchDetails.setGameDateTime(LocalDateTime.now());
        matchDetails.setScorers(new HashSet<>());

        when(matchDetailsRepository.findById(2)).thenReturn(Optional.of(matchDetails));

        var response = service.getGameById(2);

        verify(nbaService, times(0)).getApiResponse(any(), any(), any());
        assertNotNull(response);
        assertEquals(2, response.getGameId());

    }
}