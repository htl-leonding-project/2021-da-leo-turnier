package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
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
public class CompetitorRepository implements PanacheRepository<Competitor> {

    @Inject
    PlayerRepository playerRepository;

    @Inject
    TeamRepository teamRepository;

    public Competitor add(Competitor competitor) {
        if (competitor == null) {
            return null;
        }
        if (competitor.getClass() == Player.class) {
            return playerRepository.add((Player) competitor);
        } else if (competitor.getClass() == Team.class) {
            return teamRepository.add((Team) competitor);
        }
        return competitor;
    }

    public Competitor modify(long id, Competitor competitor) {
        if (competitor == null) {
            return null;
        }
        if (competitor.getClass() == Player.class) {
            return playerRepository.modify(id, (Player) competitor);
        } else if (competitor.getClass() == Team.class) {
            return teamRepository.modify(id, (Team) competitor);
        }
        return null;
    }

    public Competitor getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Competitor> getByTournamentId(Long tournamentId) {
        TypedQuery<Competitor> getById = getEntityManager().createQuery(
                "select pt.competitor " +
                        "from Participation pt " +
                        "where pt.tournament.id = :tournamentId " +
                        "order by pt.placement, pt.seed", Competitor.class);
        getById.setParameter("tournamentId", tournamentId);
        return getById.getResultList();
    }

    public List<Competitor> getAll() {
        TypedQuery<Competitor> getById = getEntityManager().createQuery(
                "select c " +
                        "from Competitor c " +
                        "order by c.id", Competitor.class);
        return getById.getResultList();
    }

    public Competitor delete(Long id) {
        Competitor competitor = getById(id);
        if (competitor == null) {
            return null;
        }
        if (competitor.getClass() == Player.class) {
            return playerRepository.delete(id);
        } else if (competitor.getClass() == Team.class) {
            return teamRepository.delete(id);
        }
        return competitor;
    }

    public long clear() {
        return playerRepository.clear() + teamRepository.clear();
    }
}
