package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.SportType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class SportTypeRepository implements PanacheRepository<SportType> {

    @Inject
    TournamentRepository tournamentRepository;

    public SportType add(SportType sportType) {
        if (sportType == null) {
            return null;
        }
        Optional<SportType> existing = find("name", sportType.getName()).singleResultOptional();
        if (existing.isPresent()) {
            return existing.get();
        }
        persist(sportType);
        return sportType;
    }

    public SportType modify(long id, SportType sportType) {
        SportType toModify = getById(id);
        if (sportType == null) {
            return null;
        }
        if (toModify != null) {
            toModify.setName(sportType.getName());
        }
        return toModify;
    }

    public SportType getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<SportType> getAll() {
        return listAll();
    }

    public SportType delete(Long id) {
        SportType sportType = getById(id);
        tournamentRepository.find("sportType", sportType).stream().forEach(t -> t.setSportType(null));
        delete("id", id);
        return sportType;
    }

    public long clear() {
        tournamentRepository.getAll().forEach(t -> t.setSportType(null));
        return deleteAll();
    }
}
