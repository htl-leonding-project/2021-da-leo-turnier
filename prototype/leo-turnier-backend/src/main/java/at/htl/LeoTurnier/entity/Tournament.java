package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "T_TOURNAMENT")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "T_ID")
    Long id;

    @Column(name = "T_NAME")
    String name;

    @Column(name = "T_START_DATE")
    LocalDate startDate;

    @Column(name = "T_END_DATE")
    LocalDate endDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "T_ST_ID")
    SportType sportType;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "T_TM_ID")
    TournamentMode tournamentMode;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "COMPETITOR_TOURNAMENT",
        joinColumns = { @JoinColumn(name = "CT_T_ID")},
        inverseJoinColumns = { @JoinColumn(name = "CT_C_ID")})
    List<Competitor> competitors;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public TournamentMode getTournamentMode() {
        return tournamentMode;
    }

    public void setTournamentMode(TournamentMode tournamentMode) {
        this.tournamentMode = tournamentMode;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }
}
