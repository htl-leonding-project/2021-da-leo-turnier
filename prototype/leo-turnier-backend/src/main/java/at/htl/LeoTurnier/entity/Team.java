package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "C_TEAM")
public class Team extends Competitor {

    @OneToMany(mappedBy = "team")
    List<Player> players;

    public Team() {
        this("");
    }

    public Team(String name) {
        this(name, new LinkedList<>());
    }

    public Team(String name, List<Player> players) {
        super(name);
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
