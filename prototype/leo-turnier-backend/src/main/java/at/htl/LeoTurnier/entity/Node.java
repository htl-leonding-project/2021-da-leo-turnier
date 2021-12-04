package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "N_NODE")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "N_SEQ")
    @Column(name = "N_ID")
    Long id;

    @OneToOne
    @JoinColumn(name = "N_M_ID")
    Match match;

    @ManyToOne
    @JoinColumn(name = "N_P_ID")
    Phase phase;

    public Node() {
        this(null, null);
    }

    public Node(Match match) {
        this(match, null);
    }

    public Node(Phase phase) {
        this(null, phase);
    }

    public Node(Match match, Phase phase) {
        this.match = match;
        this.phase = phase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
