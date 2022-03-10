package at.htl.LeoTurnier.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "C_TEAM")
public class Team extends Competitor {

    public Team() {
        this("");
    }

    public Team(String name) {
        super(name);
    }
}
