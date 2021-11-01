package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class CompetitorRepository implements PanacheRepository<Competitor> {

    public Competitor add(Competitor competitor) {
        if (competitor == null || getById(competitor.getId()) != null) {
            return null;
        }
        if (competitor.getClass() == Player.class) {
            Player player = (Player) competitor;    // save Competitor in Player variable to avoid casting
            if (player.getTeam() != null) { // as long as Player has a Team
                Team team = (Team) getById((player.getTeam().getId())); // search for a Team with the Players Teams Id in the database
                if (team == null) {     // if the Team does not exist in the database, persist it...
                    persist(player.getTeam());
                    team = (Team) getById((player.getTeam().getId()));  // get the actual Team Object from the database
                }
                player.setTeam(team);   // avoid passing detached entity to persist
            }
        } else if (competitor.getClass() == Team.class) {
            Team team = (Team) competitor;  // save Competitor in Team variable to avoid casting
            team.getPlayers().forEach(p -> {    // for each Player in the Team
                Player player = (Player) getById(p.getId());    // get the actual Team Object from the database
                if (player == null) {   // if the Player does not exist in the database, persist it...
                    persist(p);
                    player = (Player) getById(p.getId());
                }
                player.setTeam(team);   // avoid passing detached entity to persist
            });
        }
        persist(competitor);
        return competitor;
    }

    public Competitor modify(long id, Competitor competitor) {
        Competitor toModify = getById(id);
        if (competitor == null || toModify == null) {
            return null;
        }
        toModify.setName(competitor.getName());
        toModify.setTotalScore(competitor.getTotalScore());
        if (competitor.getClass() == Player.class && toModify.getClass() == Player.class) { // if both the passed Competitor and the Competitor to modify are Players
            Player playerToModify = (Player) toModify;  // save old Competitor in Player variable to avoid casting
            Player playerNew = (Player) competitor; // save new passed Competitor in Player variable to avoid casting
            playerToModify.setBirthdate(playerNew.getBirthdate());
            Team team = (Team) getById(playerNew.getTeam().getId());    // search for a Team with the Players Teams Id in the database
            if (team == null) { // if the Team does not exist in the database, persist it...
                persist(playerNew.getTeam());
                team = (Team) getById((playerNew.getTeam().getId()));
            }
            playerToModify.setTeam(team);
        } else if (competitor.getClass() == Team.class && toModify.getClass() == Team.class) {  // if both the passed Competitor and the Competitor to modify are Players
            Team teamToModify = (Team) toModify;    // save old Competitor in Team variable to avoid casting
            Team teamNew = (Team) competitor;   // save new Competitor in Team variable to avoid casting
            teamToModify.getPlayers().forEach(p -> {    // remove all the Players from the old Team
                ((Player) getById(p.getId())).setTeam(null);
            });
            if (teamNew.getPlayers() != null) {
                teamToModify.setPlayers(teamNew.getPlayers());
                teamToModify.getPlayers().forEach(p -> {    // persist all not existing Players, add all Players, that belong in this Team, to this Team
                    Player player = (Player) getById(p.getId());
                    if (player == null) {
                        persist(p);
                        player = (Player) getById(p.getId());
                    }
                    player.setTeam(teamToModify);
                });
            }
        }
        return toModify;
    }

    public Competitor getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Competitor> getAll() {
        return listAll();
    }

    public Competitor delete(Long id) {
        Competitor competitor = getById(id);
        if (competitor == null) {
            return null;
        }
        if (competitor.getClass() == Player.class) {
            Player player = (Player) competitor;
            player.getTeam().getPlayers().remove(player);   // remove Player from his Team
        } else if (competitor.getClass() == Team.class) {
            Team team = (Team) competitor;
            team.getPlayers().forEach(p -> {    // set Team of all Players from this Team to null
                p.setTeam(null);
            });
        }
        delete("id", id);
        return competitor;
    }

    public long clear() {
        return deleteAll();
    }
}
