package bv.nba.challenge.entities.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayer {

    @Id
    @GeneratedValue
    private Long scorerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(columnDefinition = "gameId", nullable = false)
    private MatchDetails match;

    private String playerName;
    private Integer points;

}
