package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@Entity
@Table(name = "C_PLAYER")
public class Player extends Competitor {

    @Column(name = "C_BIRTHDATE")
    LocalDate birthdate;

    @ManyToOne
    @JoinColumn(name = "C_C_ID")
    Team team;

    public Player() {
        this("");
    }

    public Player(String name) {
        this(name, null, null);
    }

    public Player(String name, Team team) {
        this(name, null, team);
    }

    public Player(String name, LocalDate birthdate) {
        this(name, birthdate, null);
    }

    public Player(String name, LocalDate birthdate, Team team) {
        super(name);
        this.birthdate = birthdate;
        this.team = team;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
