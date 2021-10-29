package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CompetitorRepository implements PanacheRepository<Competitor> {

    @Transactional
    public Competitor add(Competitor competitor) {
        if (competitor == null) {
            return null;
        }
        else if (competitor.getClass() == Player.class) {
            Player player = (Player) competitor;
            if (player.getTeam() != null) {
                if (getById((player.getTeam().getId())) == null) {
                    persist(player.getTeam());
                }
                player.setTeam(
                        (Team) getById((player.getTeam().getId()))
                );
            }
        } else if (competitor.getClass() == Team.class) {
            Team team = (Team) competitor;
            team.getPlayers().forEach(p -> {
                if (getById(p.getId()) == null) {
                    persist(p);
                }
                Player player = (Player) getById(p.getId());
                player.setTeam(team);
            });
        }
        persist(competitor);
        return competitor;
    }

    @Transactional
    public Competitor modify(long id, Competitor competitor) {
        if (competitor == null) {
            return null;
        }
        Competitor toModify = getById(id);
        toModify.setTournaments(competitor.getTournaments());
        toModify.setName(competitor.getName());
        toModify.setTotalScore(competitor.getTotalScore());
        if (competitor.getClass() == Player.class && toModify.getClass() == Player.class) {
            ((Player) toModify).setBirthdate(((Player) competitor).getBirthdate());
            ((Player) toModify).setTeam(((Player) competitor).getTeam());
        } else if (competitor.getClass() == Team.class && toModify.getClass() == Team.class) {
            ((Team) toModify).setPlayers(((Team) competitor).getPlayers());
        }
        return toModify;
    }

    public Competitor getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Competitor> getAll() {
        return listAll();
    }

    @Transactional
    public boolean delete(Long id) {
        return delete("id", id) != -1;
    }

    @Transactional
    public long clear() {
        return deleteAll();
    }

}
