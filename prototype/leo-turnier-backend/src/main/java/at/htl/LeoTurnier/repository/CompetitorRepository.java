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
            throw new IllegalArgumentException("Competitor is null.");
        }
        if (getById(competitor.getId()) != null) {
            throw new IllegalArgumentException("Competitor already exists.");
        }
        if (competitor.getClass() == Player.class) {
            Player player = (Player) competitor;
            if (player.getTeam() != null) {
                Team team = (Team) getById((player.getTeam().getId()));
                if (team == null) {
                    persist(player.getTeam());
                    team = (Team) getById((player.getTeam().getId()));
                }
                player.setTeam(team);
            }
        } else if (competitor.getClass() == Team.class) {
            Team team = (Team) competitor;
            team.getPlayers().forEach(p -> {
                Player player = (Player) getById(p.getId());
                if (player == null) {
                    persist(p);
                    player = (Player) getById(p.getId());
                }
                player.setTeam(team);
            });
        }
        persist(competitor);
        return competitor;
    }

    @Transactional
    public Competitor modify(long id, Competitor competitor) {
        Competitor toModify = getById(id);
        if (toModify == null) {
            throw new IllegalArgumentException("Competitor with Id " + id + " does not exist.");
        }
        if (competitor == null) {
            throw new IllegalArgumentException("Competitor is null.");
        }
        toModify.setTournaments(competitor.getTournaments());
        toModify.setName(competitor.getName());
        toModify.setTotalScore(competitor.getTotalScore());
        if (competitor.getClass() == Player.class && toModify.getClass() == Player.class) {
            Player playerToModify = (Player) toModify;
            Player playerNew = (Player) competitor;
            playerToModify.setBirthdate(playerNew.getBirthdate());
            Team team = (Team) getById(playerNew.getTeam().getId());
            if (team == null) {
                persist(playerNew.getTeam());
                team = (Team) getById((playerNew.getTeam().getId()));
            }
            playerToModify.setTeam(team);
        } else if (competitor.getClass() == Team.class && toModify.getClass() == Team.class) {
            Team teamToModify = (Team) toModify;
            Team teamNew = (Team) competitor;
            teamToModify.getPlayers().forEach(p -> {
                ((Player) getById(p.getId())).setTeam(null);
            });
            if (teamNew.getPlayers() != null) {
                teamToModify.setPlayers(teamNew.getPlayers());
                teamToModify.getPlayers().forEach(p -> {
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

    @Transactional
    public void delete(Long id) {
        Competitor competitor = getById(id);
        if (competitor == null) {
            throw new IllegalArgumentException("Competitor with Id " + id + " does not exist.");
        }
        if (competitor.getClass() == Player.class) {
            Player player = (Player) competitor;
            player.getTeam().getPlayers().remove(player);
        } else if (competitor.getClass() == Team.class) {
            Team team = (Team) competitor;
            team.getPlayers().forEach(p -> {
                p.setTeam(null);
            });
        }
        delete("id", id);
    }

    @Transactional
    public long clear() {
        return deleteAll();
    }

}
