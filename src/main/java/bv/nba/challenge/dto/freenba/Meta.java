package bv.nba.challenge.dto.freenba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("current_page")
    private Integer currentPage;
    @JsonProperty("next_page")
    private Integer nextPage;
    @JsonProperty("per_page")
    private Integer perPage;
    @JsonProperty("total_count")
    private Integer totalCount;

}
