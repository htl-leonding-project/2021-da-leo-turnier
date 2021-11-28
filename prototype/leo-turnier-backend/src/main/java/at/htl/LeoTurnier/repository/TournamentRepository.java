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
        if (tournament == null || getById(tournament.getId()) != null) {
            return null;
        }
        tournament.getCompetitors().forEach(c -> {
            competitorRepository.add(c);
        });
        if (tournament.getTournamentMode() != null) {
            tournamentModeRepository.add(tournament.getTournamentMode());
            tournament.setTournamentMode(tournamentModeRepository.getById(tournament.getTournamentMode().getId()));
        }
        if (tournament.getSportType() != null) {
            sportTypeRepository.add(tournament.getSportType());
            tournament.setSportType(sportTypeRepository.getById(tournament.getSportType().getId()));
        }
        persist(tournament);
        return tournament;
    }

    public Tournament modify(long id, Tournament tournament) {
        Tournament toModify = getById(id);
        if (tournament == null || toModify == null) {
            return null;
        }
        toModify.setName(tournament.getName());
        toModify.setStartDate(tournament.getStartDate());
        toModify.setEndDate(tournament.getEndDate());
        tournament.getCompetitors().forEach(c -> {
            competitorRepository.add(c);
        });
        if (tournament.getTournamentMode() != null) {
            tournamentModeRepository.add(tournament.getTournamentMode());
            tournament.setTournamentMode(tournamentModeRepository.getById(tournament.getTournamentMode().getId()));
        }
        if (tournament.getSportType() != null) {
            sportTypeRepository.add(tournament.getSportType());
            tournament.setSportType(sportTypeRepository.getById(tournament.getSportType().getId()));
        }
        toModify.setCompetitors(tournament.getCompetitors());
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
        phaseRepository.getAll().forEach(t -> {
            t.setTournament(null);
        });
        delete("id", id);
        return tournament;
    }

    public long clear() {
        phaseRepository.clear();
        return deleteAll();
    }
}
