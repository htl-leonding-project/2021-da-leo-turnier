package at.htl.LeoTurnier.entity;

import javax.persistence.*;

@Entity
@Table(name = "TM_TOURNAMENT_MODE")
public class TournamentMode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TM_SEQ")
    @Column(name = "TM_ID")
    Long id;

    @Column(name = "TM_NAME")
    String name;

    public TournamentMode() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
