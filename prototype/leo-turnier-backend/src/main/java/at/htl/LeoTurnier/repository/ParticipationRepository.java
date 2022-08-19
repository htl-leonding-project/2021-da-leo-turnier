package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.dto.SeedDto;
import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Participation;
import at.htl.LeoTurnier.entity.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

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

    public Participation modify(Long tournamentId, Long competitorId, Participation participation) {
        Participation toModify = getById(tournamentId, competitorId);
        if (toModify != null) {
            toModify.setPlacement(participation.getPlacement());
            toModify.setSeed(participation.getSeed());
        }
        return toModify;
    }

    public Participation modifyPlacement(Long tournamentId, Long competitorId, int placement) {
        Participation toModify = getById(tournamentId, competitorId);
        if (toModify != null) {
            toModify.setPlacement(placement);
        }
        return toModify;
    }

    public Participation modifySeed(Long tournamentId, Long competitorId, int seed) {
        Participation toModify = getById(tournamentId, competitorId);
        if (toModify != null) {
            toModify.setSeed(seed);
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

    public List<Participation> getByTournamentId(Long tournamentId) {
        TypedQuery<Participation> getById = getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.tournament.id = :tournamentId " +
                        "order by pt.placement desc, pt.seed desc", Participation.class);
        getById.setParameter("tournamentId", tournamentId);
        return getById.getResultList();
    }

    public List<Participation> getByCompetitorId(Long competitorId) {
        TypedQuery<Participation> getById = getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.competitor.id = :competitorId ", Participation.class);
        getById.setParameter("competitorId", competitorId);
        return getById.getResultList();
    }

    public List<Participation> getAll() {
        return listAll();
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

    public Tournament seedCompetitors(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if (tournament == null) {
            return null;
        }
        List<SeedDto> res = new LinkedList<>();
        List<Competitor> competitors = competitorRepository.getByTournamentId(tournamentId);
        for (Competitor competitor : competitors) {
            List<Participation> competitorParticipations = getByCompetitorId(competitor.getId());
            double sumOfPlacement = competitorParticipations.stream()
                    .map(Participation::getPlacement)
                    .reduce(0, Integer::sum);
            res.add(new SeedDto(competitor.getId(), sumOfPlacement / competitorParticipations.size()));
        }

        res.sort((s1, s2) -> {
            if (s1.getSeed() > 0 && s2.getSeed() == 0) {
                return -1;
            } else if (s1.getSeed() == 0 && s2.getSeed() > 0) {
                return 1;
            } else if (s1.getSeed() < s2.getSeed()) {
                return -1;
            } else if (s1.getSeed() > s2.getSeed()) {
                return 1;
            }
            return 0;
        });

        for (int i = 0; i < res.size(); i++) {
            modifySeed(tournamentId, res.get(i).getId(), i);
        }
        return tournament;
    }

    public Tournament orderCompetitors(Long tournamentId, List<Competitor> competitors) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if (tournament == null) {
            return null;
        }
        List<Competitor> competitorsOld = competitorRepository.getByTournamentId(tournamentId);
        if (competitors.size() != competitorsOld.size()) {
            return null;
        }
        for (Competitor competitor : competitors) {
            if (competitorsOld.stream().noneMatch(c -> c.getId().equals(competitor.getId()))) {
                return null;
            }
        }
        for (int i = 0; i < competitors.size(); i++) {
            modifySeed(tournamentId, competitors.get(i).getId(), i);
        }
        return tournament;
    }
}
