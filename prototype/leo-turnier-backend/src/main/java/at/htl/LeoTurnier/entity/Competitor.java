package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "C_COMPETITOR")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "C_COMPETITOR_TYPE")
public abstract class Competitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "C_ID")
    private Long id;

    @Column(name = "C_NAME")
    String name;

    @Column(name = "C_TOTAL_SCORE")
    int totalScore;

    public Competitor() {
        this("");
    }

    public Competitor(String name) {
        this.name = name;
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

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
