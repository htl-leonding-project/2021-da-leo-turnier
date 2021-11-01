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

    public SportType() {
        this("");
    }

    public SportType(String name) {
        this.name = name;
    }

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
