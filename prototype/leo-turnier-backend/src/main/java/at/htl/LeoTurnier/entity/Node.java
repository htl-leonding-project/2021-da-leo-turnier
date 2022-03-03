package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "N_NODE")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "N_SEQ")
    @Column(name = "N_ID")
    Long id;

    @Column(name = "N_NODE_NUMBER")
    int nodeNumber;

    @OneToOne
    @JoinColumn(name = "N_M_ID")
    Match match;

    @ManyToOne
    @JoinColumn(name = "N_P_ID")
    Phase phase;

    public Node() {
        this(-1);
    }

    public Node(int nodeNumber) {
        this(nodeNumber, null, null);
    }

    public Node(int nodeNumber, Match match) {
        this(nodeNumber, match, null);
    }

    public Node(int nodeNumber, Phase phase) {
        this(nodeNumber, null, phase);
    }

    public Node(int nodeNumber, Match match, Phase phase) {
        this.nodeNumber = nodeNumber;
        this.match = match;
        this.phase = phase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
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
