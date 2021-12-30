package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class TeamRepository implements PanacheRepository<Team> {

    @Inject
    MatchRepository matchRepository;

    @Inject
    PlayerRepository playerRepository;

    public Team add(Team team) {
        if (team == null) {
            return null;
        }
        Team existing = getById(team.getId());
        if (existing != null) {
            return existing;
        }
        team.getPlayers().forEach(p -> {
            Player player = playerRepository.add(p);
            player.setTeam(team);
        });
        persist(team);
        return team;
    }

    public Team modify(long id, Team team) {
        Team toModify = findById(id);
        if (team == null) {
            return null;
        }
        if (toModify != null) {
            toModify.getPlayers().forEach(p -> playerRepository.getById(p.getId()).setTeam(null));
            toModify.setName(team.getName());
            toModify.setTotalScore(team.getTotalScore());
            if (team.getPlayers() != null) {
                toModify.setPlayers(team.getPlayers());
                toModify.getPlayers().forEach(p -> {
                    Player player = playerRepository.getById(p.getId());
                    if (player == null) {
                        playerRepository.add(p);
                        player = playerRepository.getById(p.getId());
                    }
                    player.setTeam(toModify);
                });
            }
        }
        return toModify;
    }

    public Team getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Team> getAll() {
        return listAll();
    }

    public Team delete(Long id) {
        Team team = getById(id);
        List<Long> matchIds = getEntityManager().createQuery("select m.id from Match m where m.competitor1.id = :id or m.competitor2.id = :id", Long.class)
                .setParameter("id", id)
                .getResultList();
        for (Long matchId : matchIds) {
            matchRepository.delete(matchId);
        }
        team.getPlayers().forEach(p -> p.setTeam(null));
        delete("id", id);
        return team;
    }

    public long clear() {
        int count = 0;
        for (Team t : getAll()) {
            delete(t.getId());
            count++;
        }
        return count;
    }
}