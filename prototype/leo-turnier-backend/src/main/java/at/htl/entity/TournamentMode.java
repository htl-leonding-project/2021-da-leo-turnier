package at.htl.entity;

import javax.persistence.*;

@Entity
public class TournamentMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TM_ID")
    Long id;

    @Column(name = "TM_NAME")
    String name;
}
