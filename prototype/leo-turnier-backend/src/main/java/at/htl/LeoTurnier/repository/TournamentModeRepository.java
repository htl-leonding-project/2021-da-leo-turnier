package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.entity.TournamentMode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TournamentModeRepository implements PanacheRepository<TournamentMode> {

    public TournamentMode getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<TournamentMode> getAll() {
        return listAll();
    }
}
