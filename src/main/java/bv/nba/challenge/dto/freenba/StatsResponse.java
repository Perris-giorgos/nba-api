package bv.nba.challenge.dto.freenba;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatsResponse extends PaginationInfo {


    List<PlayerStats> data = new ArrayList<>();
}
