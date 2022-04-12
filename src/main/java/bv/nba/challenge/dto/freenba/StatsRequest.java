package bv.nba.challenge.dto.freenba;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class StatsRequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> gameIds;
    private Integer page;

}
