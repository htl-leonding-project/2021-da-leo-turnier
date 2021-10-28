package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TournamentRepository implements PanacheRepository<Tournament> {

    @Transactional
    public Tournament add(Tournament tournament) {
        persist(tournament);
        return tournament;
    }

    @Transactional
    public Tournament modify(Tournament tournament) {
        getEntityManager().merge(tournament);
        return tournament;
    }

    public Tournament getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Tournament> getAll() {
        return listAll();
    }

    @Transactional
    public boolean delete(Long id) {
        return delete("id", id) != -1;
    }

    public long clear() {
        getEntityManager().createNativeQuery("ALTER TABLE T_TOURNAMENT AUTO_INCREMENT = 1");
        return deleteAll();
    }
}
