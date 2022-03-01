package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Participation;
import at.htl.LeoTurnier.entity.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.postgresql.core.NativeQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.management.Query;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ParticipationRepository implements PanacheRepository<Participation> {

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    CompetitorRepository competitorRepository;

    public Participation add(Long tournamentId, Long competitorId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        Competitor competitor = competitorRepository.getById(competitorId);
        if (tournament == null || competitor == null) {
            return null;
        }
        Participation existing = getById(tournamentId, competitorId);
        if (existing != null) {
            return existing;
        }
        Participation participation = new Participation(tournament, competitor);

        persist(participation);
        return participation;
    }

    public Participation modify(Long tournamentId, Long competitorId, int placement) {
        Participation toModify = getById(tournamentId, competitorId);
        if (toModify != null) {
            toModify.setPlacement(placement);
        }
        return toModify;
    }

    public Participation getById(Long tournamentId, Long competitorId) {
        TypedQuery<Participation> getById = getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.tournament.id = :tournamentId " +
                        "and pt.competitor.id = :competitorId", Participation.class);
        getById.setParameter("tournamentId", tournamentId);
        getById.setParameter("competitorId", competitorId);
        return getById.getResultList().stream()
                .findFirst()
                .orElse(null);
    }

    public List<Participation> getByTournament(Long tournamentId) {
        TypedQuery<Participation> getById = getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.tournament.id = :tournamentId ", Participation.class);
        getById.setParameter("tournamentId", tournamentId);
        return new ArrayList<>(getById.getResultList());
    }

    public List<Participation> getByCompetitor(Long competitorId) {
        TypedQuery<Participation> getById = getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.competitor.id = :competitorId ", Participation.class);
        getById.setParameter("competitorId", competitorId);
        return new ArrayList<>(getById.getResultList());
    }

    public Participation delete(Long tournamentId, Long competitorId) {
        Participation participation = getById(tournamentId, competitorId);
        if (participation == null) {
            return null;
        }

        delete(participation);
        return participation;
    }

    public long clear() {
        return deleteAll();
    }
}
