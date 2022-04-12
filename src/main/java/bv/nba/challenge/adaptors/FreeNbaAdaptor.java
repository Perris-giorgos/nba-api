package bv.nba.challenge.adaptors;

import bv.nba.challenge.dto.freenba.StatsRequest;
import bv.nba.challenge.dto.freenba.StatsResponse;
import bv.nba.challenge.exceptions.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
@Slf4j
public class FreeNbaAdaptor {

    private static final String HOST_HEADER = "X-RapidAPI-Host";
    private static final String KEY_HEADER = "X-RapidAPI-Key";
    private final RestTemplate nbaTemplate;
    private final String statsUrl;

    public FreeNbaAdaptor(RestTemplate nbaTemplate, @Value("${nba.statsUrl}") String statsUrl) {
        this.nbaTemplate = nbaTemplate;
        this.statsUrl = statsUrl;
    }

    public Optional<StatsResponse> callNbaApi(StatsRequest request) {

        String finalRequest = createRequestWithParameters(request, statsUrl);
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entityRequest = new HttpEntity<>(headers);

        log.info("Calling Free NBA API with request " + finalRequest);

        ResponseEntity<StatsResponse> apiResponseEntity;
        try {
            apiResponseEntity = nbaTemplate.exchange(finalRequest, HttpMethod.GET, entityRequest, StatsResponse.class);
        } catch (HttpStatusCodeException e) {
            log.info("Failed to retrieve data from Free NBA API");
            throw new ExternalApiException(e.getMessage(), e.getStatusCode());
        }
        log.info("callNbaApi response: " + apiResponseEntity);

        return Optional.ofNullable(apiResponseEntity.getBody());

    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HOST_HEADER, "free-nba.p.rapidapi.com");
        headers.set(KEY_HEADER, "9744d9350dmsh9e299edf99878e7p170569jsn2058561660bd");

        return headers;
    }

    private String createRequestWithParameters(StatsRequest request, String statsUrl) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(statsUrl);

        if (request.getEndDate() != null
            && request.getStartDate() != null) {
            uriBuilder.queryParam("end_date", request.getEndDate());
            uriBuilder.queryParam("start_date", request.getStartDate());

        }
        if (!request.getGameIds().isEmpty()) {
            request.getGameIds().forEach(id ->
                uriBuilder.queryParam("game_ids[]", id));
        }
        if (request.getPage() != null) {
            uriBuilder.queryParam("page", request.getPage());
        }

        // request available results per page
        uriBuilder.queryParam("per_page", "100");

        return uriBuilder.toUriString().replace("%5B%5D", "[]");

    }


}
