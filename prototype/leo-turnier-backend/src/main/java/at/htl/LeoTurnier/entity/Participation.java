package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
class ParticipationId implements Serializable {

    Long tournamentId;

    Long competitorId;

    public ParticipationId() {
    }

    public ParticipationId(Long tournamentId, Long competitorId) {
        this.tournamentId = tournamentId;
        this.competitorId = competitorId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Long competitorId) {
        this.competitorId = competitorId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

@Entity
@Table(name = "PT_PARTICIPATION")
public class Participation implements Comparable<Participation> {

    @EmbeddedId
    ParticipationId id;

    @ManyToOne
    @JoinColumn(name = "PT_T_ID")
    @MapsId("tournamentId")
    Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "PT_C_ID")
    @MapsId("competitorId")
    Competitor competitor;

    @Column(name = "PT_PLACEMENT")
    int placement;

    @Column(name = "PT_SEED")
    int seed;

    public Participation() {
        this(null, null);
    }

    public Participation(Tournament tournament, Competitor competitor) {
        this.tournament = tournament;
        this.competitor = competitor;
        this.id = new ParticipationId();
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }


    @Override
    public int compareTo(Participation p) {
        if (this.getSeed() > 0 && p.getSeed() == 0) {
            return -1;
        } else if (this.getSeed() == 0 && p.getSeed() > 0) {
            return 1;
        } else if (this.getSeed() < p.getSeed()) {
            return -1;
        } else if (this.getSeed() > p.getSeed()) {
            return 1;
        }
        return 0;
    }
}
