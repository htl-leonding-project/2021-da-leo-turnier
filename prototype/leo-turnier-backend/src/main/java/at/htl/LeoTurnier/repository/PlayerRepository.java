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
        if (player == null) {
            return null;
        }
        Player existing = getById(player.getId());
        if (existing != null) {
            return existing;
        }
        player.setTeam(
                teamRepository.add(player.getTeam()));
        persist(player);
        return player;
    }

    public Player modify(long id, Player player) {
        Player toModify = getById(id);
        if (player == null) {
            return null;
        }
        if (toModify != null) {
            toModify.setTeam(
                    teamRepository.add(player.getTeam()));
            toModify.setName(player.getName());
            toModify.setSeed(player.getSeed());
            toModify.setBirthdate(player.getBirthdate());
        }
        return toModify;
    }

    public Player getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Player> getAll() {
        return listAll();
    }

    public List<Player> getByTeamId(Long teamId) {
        return getEntityManager().createQuery("select p from Player p where p.team.id = :teamId", Player.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }

    public Player delete(Long id) {
        Player player = getById(id);
        List<Long> matchIds = getEntityManager().createQuery("select m.id from Match m where m.competitor1.id = :id or m.competitor2.id = :id", Long.class)
                .setParameter("id", id)
                .getResultList();
        for (Long matchId : matchIds) {
            matchRepository.delete(matchId);
        }
        getEntityManager().createQuery("delete from Participation p where p.competitor.id = :id")
                .setParameter("id", id)
                .executeUpdate();
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
