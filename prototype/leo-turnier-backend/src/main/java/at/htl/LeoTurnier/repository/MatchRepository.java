package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.entity.Phase;
import at.htl.LeoTurnier.entity.SportType;
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
        if (match == null || getById(match.getId()) != null) {
            return null;
        }
        if (match.getCompetitor1() != null) {
            competitorRepository.add(match.getCompetitor1());
            match.setCompetitor1(competitorRepository.getById(match.getCompetitor1().getId()));
        }
        if (match.getCompetitor2() != null) {
            competitorRepository.add(match.getCompetitor2());
            match.setCompetitor2(competitorRepository.getById(match.getCompetitor2().getId()));
        }
        persist(match);
        return match;
    }

    public Match modify(long id, Match match) {
        Match toModify = getById(id);
        if (match == null || toModify == null) {
            return null;
        }
        return toModify;
    }

    public Match getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Match> getAll() {
        return listAll();
    }

    public Match delete(Long id) {
        Match match = getById(id);
        delete("id", id);
        return match;
    }

    public long clear() {
        return deleteAll();
    }
}
