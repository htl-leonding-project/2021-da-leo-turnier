package at.htl.LeoTurnier.entity;

import at.htl.LeoTurnier.repository.LocalDateAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.LinkedList;
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
    @XmlJavaTypeAdapter(type=LocalDate.class, value= LocalDateAdapter.class)
    LocalDate startDate;

    @Column(name = "T_END_DATE")
    @XmlJavaTypeAdapter(type=LocalDate.class, value= LocalDateAdapter.class)
    LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "T_ST_ID")
    SportType sportType;

    @ManyToOne
    @JoinColumn(name = "T_TM_ID")
    TournamentMode tournamentMode;

    @ManyToMany
    @JoinTable(name = "CT_COMPETITOR_TOURNAMENT",
        joinColumns = { @JoinColumn(name = "CT_T_ID")},
        inverseJoinColumns = { @JoinColumn(name = "CT_C_ID")})
    List<Competitor> competitors;

    public Tournament() {
        this("");
    }

    public Tournament(String name) {
        this(name, new LinkedList<>());
    }

    public Tournament(String name, List<Competitor> competitors) {
        this(name, null, null, null, null, competitors);
    }

    public Tournament(String name, SportType sportType) {
        this(name, sportType, null);
    }

    public Tournament(String name, TournamentMode tournamentMode) {
        this(name, null, tournamentMode);
    }

    public Tournament(String name, SportType sportType, TournamentMode tournamentMode) {
        this(name, sportType, tournamentMode, new LinkedList<>());
    }

    public Tournament(String name, SportType sportType, TournamentMode tournamentMode, List<Competitor> competitors) {
        this(name, null, sportType, tournamentMode, competitors);
    }

    public Tournament(String name, LocalDate startDate, SportType sportType, TournamentMode tournamentMode, List<Competitor> competitors) {
        this(name, startDate, null, sportType, tournamentMode, competitors);
    }

    public Tournament(String name, LocalDate startDate, LocalDate endDate, SportType sportType, TournamentMode tournamentMode, List<Competitor> competitors) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sportType = sportType;
        this.tournamentMode = tournamentMode;
        this.competitors = competitors;
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
