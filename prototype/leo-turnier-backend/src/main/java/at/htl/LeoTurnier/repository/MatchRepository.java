package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Match;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class MatchRepository implements PanacheRepository<Match> {

    @Inject
    NodeRepository nodeRepository;

    @Inject
    CompetitorRepository competitorRepository;

    public Match add(Match match) {
        if (match == null) {
            return null;
        }
        Match existing = getById(match.getId());
        if (existing != null) {
            return existing;
        }
        match.setCompetitor1(
                competitorRepository.add(match.getCompetitor1()));
        match.setCompetitor2(
                competitorRepository.add(match.getCompetitor2()));
        persist(match);
        return match;
    }

    public Match modify(long id, Match match) {
        Match toModify = getById(id);
        if (match == null) {
            return null;
        }
        if (toModify != null) {
            match.setCompetitor1(
                    competitorRepository.add(match.getCompetitor1()));
            match.setCompetitor2(
                    competitorRepository.add(match.getCompetitor2()));
            toModify.setCompetitor1(match.getCompetitor1());
            toModify.setCompetitor2(match.getCompetitor2());
            toModify.setDate(match.getDate());
            toModify.setScore1(match.getScore1());
            toModify.setScore2(match.getScore2());
            toModify.setFinished(match.isFinished());
        }
        return toModify;
    }

    public Match getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Match> getByTournamentId(Long tournamentId) {
        return getEntityManager()
                .createQuery("select m from Match m join Node n on m.id = n.match.id " +
                        "where n.phase.tournament.id = :tournamentId " +
                        "and m.competitor1 is not null " +
                        "and m.competitor2 is not null " +
                        "order by m.isFinished, m.date", Match.class)
                .setParameter("tournamentId", tournamentId)
                .getResultList();
    }

    public List<Match> getByTournamentGroup(Long tournamentId, int groupNumber) {
        return getEntityManager()
                .createQuery("select m from Match m join Node n on m.id = n.match.id "  +
                        "where n.phase.tournament.id = :tournamentId " +
                        "and m.competitor1 is not null " +
                        "and m.competitor2 is not null " +
                        "and n.phase.groupNumber = :groupNumber " +
                        "order by n.phase.phaseNumber, m.isFinished, m.date", Match.class)
                .setParameter("tournamentId", tournamentId)
                .setParameter("groupNumber", groupNumber)
                .getResultList();
    }

    public List<Match> getAll() {
        return listAll();
    }

    public Match delete(Long id) {
        Match match = getById(id);
        nodeRepository.find("match", match).stream().forEach(p -> nodeRepository.delete(p));
        delete("id", id);
        return match;
    }

    public long clear() {
        nodeRepository.clear();
        return deleteAll();
    }
}
