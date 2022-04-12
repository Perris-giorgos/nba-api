package bv.nba.challenge.entities.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class MatchDetails {

    @Id
    private Integer gameId;

    private LocalDateTime gameDateTime;

    private String homeTeamName;

    private Integer homeTeamScore;

    private String visitorTeamName;

    private Integer visitorTeamScore;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private Set<MatchPlayer> scorers;

}
