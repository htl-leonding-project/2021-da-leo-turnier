package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "N_NODE")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N_ID")
    Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "N_P_ID")
    Phase phase;

    @OneToOne
    @JoinColumn(name = "N_M_ID")
    Match match;
}
