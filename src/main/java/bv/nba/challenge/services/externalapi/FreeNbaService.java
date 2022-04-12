package bv.nba.challenge.services.externalapi;

import bv.nba.challenge.adaptors.FreeNbaAdaptor;
import bv.nba.challenge.dto.freenba.StatsRequest;
import bv.nba.challenge.dto.freenba.StatsResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

@Service
@AllArgsConstructor
public class FreeNbaService {

    private final FreeNbaAdaptor freeNbaAdaptor;

    /**
     * method to create the appropriate request to call Free NBA API.
     *
     * @param date requested date of games
     * @param gameId id of the requested date
     */
    public StatsResponse getApiResponse(LocalDate date, Integer gameId, Integer page) {

        var requestParams = createApiRequestParams(date, gameId, page);

        var response = freeNbaAdaptor.callNbaApi(requestParams);

        return response.orElse(new StatsResponse());

    }

    private StatsRequest createApiRequestParams(LocalDate date, Integer gameId, Integer page) {
        StatsRequest request = new StatsRequest();
        // although start and end dates are set free nba api will not return the correct games for the date. Using
        // the day before it seems that it returns a bit better results
        if (date != null) {
            var newDate = date.minusDays(1);
            request.setEndDate(newDate);
            request.setStartDate(newDate);
        }
        request.setGameIds(gameId != null ? Collections.singletonList(gameId) : new ArrayList<>());
        request.setPage(page);

        return request;
    }

}
