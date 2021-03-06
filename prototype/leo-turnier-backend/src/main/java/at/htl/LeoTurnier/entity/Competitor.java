package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "C_COMPETITOR")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Competitor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "C_SEQ")
    @Column(name = "C_ID")
    Long id;

    @Column(name = "C_NAME")
    String name;

    @Column(name = "C_SEED")
    double seed;

    public Competitor() {
        this("");
    }

    public Competitor(String name) {
        this.name = name;
        this.seed = -1;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSeed() {
        return seed;
    }

    public void setSeed(double seed) {
        this.seed = seed;
    }
}
