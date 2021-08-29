package at.htl.entity;

import javax.persistence.*;

@Entity
public class SportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ST_ID")
    Long id;

    @Column(name = "ST_NAME")
    String name;
}
