package bv.nba.challenge.repositories;

import bv.nba.challenge.entities.ActionsAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionsAuditRepository extends JpaRepository<ActionsAudit, Long> {


}
