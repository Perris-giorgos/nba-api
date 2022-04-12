package bv.nba.challenge.repositories;

import bv.nba.challenge.entities.cache.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {

}
