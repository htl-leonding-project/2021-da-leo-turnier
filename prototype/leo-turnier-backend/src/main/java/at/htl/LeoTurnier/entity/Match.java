package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "M_MATCH")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "M_ID")
    Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "M_C_ID_1")
    Competitor competitor1;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "M_C_ID_2")
    Competitor competitor2;

    @Column(name = "M_DATE")
    LocalDateTime date;

    @Column(name = "M_SCORE_1")
    int score1;

    @Column(name = "M_SCORE_2")
    int score2;

    public Match() {
        this(null, null);
    }

    public Match(Competitor competitor1, Competitor competitor2) {
        this(competitor1, competitor2, null);
    }

    public Match(Competitor competitor1, Competitor competitor2, LocalDateTime date) {
        this.competitor1 = competitor1;
        this.competitor2 = competitor2;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competitor getCompetitor1() {
        return competitor1;
    }

    public void setCompetitor1(Competitor competitor1) {
        this.competitor1 = competitor1;
    }

    public Competitor getCompetitor2() {
        return competitor2;
    }

    public void setCompetitor2(Competitor competitor2) {
        this.competitor2 = competitor2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }
}
