package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "N_NODE")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N_ID")
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "N_P_ID")
    Phase phase;

    @OneToOne
    @JoinColumn(name = "N_M_ID")
    Match match;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
