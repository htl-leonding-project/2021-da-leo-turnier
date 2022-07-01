package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "P_PHASE")
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "P_SEQ")
    @Column(name = "P_ID")
    Long id;

    @Column(name = "P_PHASE_NUMBER")
    int phaseNumber;

    @Column(name = "P_GROUP_NUMBER")
    int groupNumber;

    @ManyToOne
    @JoinColumn(name = "P_T_ID")
    Tournament tournament;

    public Phase() {
        this(-1);
    }

    public Phase(int phaseNumber) {
        this(phaseNumber, -1);
    }

    public Phase(int phaseNumber, int groupNumber) {
        this(phaseNumber, groupNumber, null);
    }

    public Phase(int phaseNumber, Tournament tournament) {
        this(phaseNumber, -1, tournament);
    }

    public Phase(int phaseNumber, int groupNumber, Tournament tournament) {
        this.phaseNumber = phaseNumber;
        this.groupNumber = groupNumber;
        this.tournament = tournament;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
