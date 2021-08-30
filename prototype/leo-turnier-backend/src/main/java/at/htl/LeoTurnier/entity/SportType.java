package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "ST_SPORT_TYPE")
public class SportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ST_ID")
    Long id;

    @Column(name = "ST_NAME")
    String name;
}
