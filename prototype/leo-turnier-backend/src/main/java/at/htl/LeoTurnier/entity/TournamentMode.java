package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "TM_TOURNAMENT_MODE")
public class TournamentMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TM_ID")
    Long id;

    @Column(name = "TM_NAME")
    String name;
}
