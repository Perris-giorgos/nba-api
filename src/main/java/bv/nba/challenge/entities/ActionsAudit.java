package bv.nba.challenge.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class ActionsAudit {


    @Id
    @GeneratedValue
    private Long actionId;

    private String action;
    private String status;


}
