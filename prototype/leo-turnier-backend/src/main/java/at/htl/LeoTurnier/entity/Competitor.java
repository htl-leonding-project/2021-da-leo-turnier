package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "C_COMPETITOR")
public class Competitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "C_ID")
    private Long id;

    @Column(name = "C_NAME")
    String name;

    @Column(name = "C_TOTAL_SCORE")
    int totalScore;

    @ManyToMany(mappedBy = "competitors", cascade = CascadeType.ALL)
    List<Tournament> tournaments;

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
