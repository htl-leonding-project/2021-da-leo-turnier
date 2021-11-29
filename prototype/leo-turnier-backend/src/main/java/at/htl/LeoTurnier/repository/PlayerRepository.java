package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class PlayerRepository implements PanacheRepository<Player> {

    @Inject
    MatchRepository matchRepository;

    @Inject
    TeamRepository teamRepository;

    public Player add(Player player) {
        if (player == null || getById(player.getId()) != null) {
            return null;
        }
        if (player.getTeam() != null) {
            Team team = teamRepository.getById((player.getTeam().getId()));
            if (team == null) {
                teamRepository.add(player.getTeam());
                team = teamRepository.getById((player.getTeam().getId()));
            }
            player.setTeam(team);
        }
        persist(player);
        return player;
    }

    public Player modify(long id, Player player) {
        Player toModify = getById(id);
        if (player == null || toModify == null) {
            return null;
        }
        toModify.setName(player.getName());
        toModify.setTotalScore(player.getTotalScore());
        toModify.setBirthdate(player.getBirthdate());
        Team team = teamRepository.getById(player.getTeam().getId());
        if (team == null) {
            teamRepository.persist(player.getTeam());
            team = teamRepository.getById((player.getTeam().getId()));
        }
        toModify.setTeam(team);
        return toModify;
    }

    public Player getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Player> getAll() {
        return listAll();
    }

    public Player delete(Long id) {
        Player player = getById(id);
        getEntityManager().createQuery("delete from Match m where m.competitor1.id = :id or m.competitor2.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        if (player.getTeam() != null) {
            player.getTeam().getPlayers().remove(player);
        }
        delete("id", id);
        return player;
    }

    public long clear() {
        int count = 0;
        for (Player p : getAll()) {
            delete(p.getId());
            count++;
        }
        return count;
    }
}
