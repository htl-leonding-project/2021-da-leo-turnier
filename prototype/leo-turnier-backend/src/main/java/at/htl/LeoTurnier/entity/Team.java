package at.htl.LeoTurnier.entity;

import javax.persistence.Column;
import java.util.List;

public class Team extends Competitor {

    @Column(name = "C_PLAYERS")
    List<String> players;

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
