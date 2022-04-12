package bv.nba.challenge.repositories;

import bv.nba.challenge.entities.cache.MatchDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchDetailsRepository extends JpaRepository<MatchDetails, Integer> {

}
