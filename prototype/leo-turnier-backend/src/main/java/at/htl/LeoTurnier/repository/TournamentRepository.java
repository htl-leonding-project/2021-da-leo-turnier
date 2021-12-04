package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class TournamentRepository implements PanacheRepository<Tournament> {

    @Inject
    CompetitorRepository competitorRepository;

    @Inject
    TournamentModeRepository tournamentModeRepository;

    @Inject
    SportTypeRepository sportTypeRepository;

    @Inject
    PhaseRepository phaseRepository;

    public Tournament add(Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        Tournament existing = getById(tournament.getId());
        if (existing != null) {
            return existing;
        }
        tournament.getCompetitors().forEach(c -> competitorRepository.add(c));
        tournamentModeRepository.add(tournament.getTournamentMode());
        sportTypeRepository.add(tournament.getSportType());

        persist(tournament);
        return tournament;
    }

    public Tournament modify(long id, Tournament tournament) {
        Tournament toModify = getById(id);
        if (tournament == null) {
            return null;
        }
        if (toModify != null) {
            toModify.setName(tournament.getName());
            toModify.setStartDate(tournament.getStartDate());
            toModify.setEndDate(tournament.getEndDate());
            tournament.getCompetitors().forEach(c -> competitorRepository.add(c));
            tournamentModeRepository.add(tournament.getTournamentMode());
            sportTypeRepository.add(tournament.getSportType());
            toModify.setCompetitors(tournament.getCompetitors());
        }
        return toModify;
    }

    public Tournament getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Tournament> getAll() {
        return listAll();
    }

    public Tournament delete(Long id) {
        Tournament tournament = getById(id);
        phaseRepository.getAll().forEach(t -> phaseRepository.delete(t.getId()));
        delete("id", id);
        return tournament;
    }

    public long clear() {
        phaseRepository.clear();
        return deleteAll();
    }
}
