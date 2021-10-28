package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CompetitorRepository implements PanacheRepository<Competitor> {

    @Transactional
    public Competitor add(Competitor competitor) {
        persist(competitor);
        return competitor;
    }

    @Transactional
    public Competitor modify(Competitor competitor) {
        getEntityManager().merge(competitor);
        return competitor;
    }

    public Competitor getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Competitor> getAll() {
        return listAll();
    }

    @Transactional
    public boolean delete(Long id) {
        return delete("id", id) != -1;
    }

    public long clear() {
        getEntityManager().createNativeQuery("ALTER TABLE C_COMPETITOR AUTO_INCREMENT = 1");
        return deleteAll();
    }

}
