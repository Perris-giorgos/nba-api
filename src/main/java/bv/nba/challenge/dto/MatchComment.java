package bv.nba.challenge.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MatchComment {

    private Long id;
    private String comment;
    private LocalDateTime timestampCreated;

}
