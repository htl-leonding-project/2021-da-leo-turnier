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

    @OneToOne
    @JoinColumn(name = "N_N_ID")
    Node nextNode;

    public Node() {
        this(-1);
    }

    public Node(int nodeNumber) {
        this(nodeNumber, null, null, null);
    }

    public Node(Match match) {
        this(match, null);
    }

    public Node(Phase phase) {
        this(null, phase);
    }

    public Node(Match match, Phase phase) {
        this(-1, match, phase, null);
    }

    public Node(Node nextNode) {
        this(-1, null, null, nextNode);
    }

    public Node(int nodeNumber, Match match) {
        this(nodeNumber, match, null, null);
    }

    public Node(int nodeNumber, Phase phase) {
        this(nodeNumber, null, phase, null);
    }

    public Node(int nodeNumber, Node nextNode) {
        this(nodeNumber, null, null,nextNode);
    }

    public Node(int nodeNumber, Match match, Phase phase) {
        this(nodeNumber, match, phase, null);
    }

    public Node(int nodeNumber, Match match, Phase phase, Node nextNode) {
        this.nodeNumber = nodeNumber;
        this.match = match;
        this.phase = phase;
        this.nextNode = nextNode;
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

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
