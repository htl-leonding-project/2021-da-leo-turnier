package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.entity.TournamentMode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class TournamentModeRepository implements PanacheRepository<TournamentMode> {

    @Inject
    TournamentRepository tournamentRepository;

    public TournamentMode add(TournamentMode tournamentMode) {
        if (tournamentMode == null) {
            return null;
        }
        TournamentMode existing = getById(tournamentMode.getId());
        if (existing != null) {
            return existing;
        }
        persist(tournamentMode);
        return tournamentMode;
    }

    public TournamentMode modify(long id, TournamentMode tournamentMode) {
        TournamentMode toModify = getById(id);
        if (tournamentMode == null) {
            return null;
        }
        if (toModify != null) {
            toModify.setName(tournamentMode.getName());
        }
        return toModify;
    }

    public TournamentMode getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<TournamentMode> getAll() {
        return listAll();
    }

    public TournamentMode delete(Long id) {
        TournamentMode tournamentMode = getById(id);
        tournamentRepository.find("tournamentMode", tournamentMode).stream().forEach(t -> t.setTournamentMode(null));
        delete("id", id);
        return tournamentMode;
    }

    public long clear() {
        tournamentRepository.getAll().forEach(t -> t.setTournamentMode(null));
        return deleteAll();
    }
}
