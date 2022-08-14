package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "T_TOURNAMENT")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "T_SEQ")
    @Column(name = "T_ID")
    Long id;

    @Column(name = "T_NAME")
    String name;

    @Column(name = "T_START_DATE")
    LocalDate startDate;

    @Column(name = "T_END_DATE")
    LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "T_ST_ID")
    SportType sportType;

    @ManyToOne
    @JoinColumn(name = "T_TM_ID")
    TournamentMode tournamentMode;

    @Column(name = "T_IS_FINISHED")
    boolean isFinished;

    public Tournament() {
        this("");
    }

    public Tournament(String name) {
        this(name, null, null);
    }

    public Tournament(String name, SportType sportType) {
        this(name, sportType, null);
    }

    public Tournament(String name, TournamentMode tournamentMode) {
        this(name, null, tournamentMode);
    }

    public Tournament(String name, SportType sportType, TournamentMode tournamentMode) {
        this(name, null, sportType, tournamentMode);
    }

    public Tournament(String name, LocalDate startDate, SportType sportType, TournamentMode tournamentMode) {
        this.name = name;
        this.startDate = startDate;
        this.sportType = sportType;
        this.tournamentMode = tournamentMode;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
