package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.TournamentMode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TournamentModeRepository implements PanacheRepository<TournamentMode> {

    @Transactional
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

    public TournamentMode getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<TournamentMode> getAll() {
        return listAll();
    }
}
