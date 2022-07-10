package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Phase;
import at.htl.LeoTurnier.entity.Player;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class PhaseRepository implements PanacheRepository<Phase> {

    @Inject
    NodeRepository nodeRepository;

    @Inject
    TournamentRepository tournamentRepository;

    public Phase add(Phase phase) {
        if (phase == null) {
            return null;
        }
        Phase existing = getById(phase.getId());
        if (existing != null) {
            return existing;
        }
        phase.setTournament(
                tournamentRepository.add(phase.getTournament()));
        persist(phase);
        return phase;
    }

    public Phase modify(long id, Phase phase) {
        Phase toModify = getById(id);
        if (phase == null) {
            return null;
        }
        if (toModify != null) {
            toModify.setTournament(
                    tournamentRepository.add(phase.getTournament()));
            toModify.setPhaseNumber(phase.getPhaseNumber());
            toModify.setGroupNumber(phase.getGroupNumber());
            toModify.setTournament(phase.getTournament());
        }
        return toModify;
    }

    public Phase getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Phase> getByTournamentId(Long tournamentId) {
        return getEntityManager()
                .createQuery("select p from Phase p where p.tournament.id = :tournamentId order by p.phaseNumber",
                        Phase.class)
                .setParameter("tournamentId", tournamentId)
                .getResultList();
    }

    public List<Phase> getByTournamentGroup(Long tournamentId, int groupNumber) {
        return getEntityManager()
                .createQuery("select p from Phase p where p.tournament.id = :tournamentId " +
                                "and p.groupNumber = :groupNumber order by p.phaseNumber",
                        Phase.class)
                .setParameter("tournamentId", tournamentId)
                .setParameter("groupNumber", groupNumber)
                .getResultList();
    }

    public List<Phase> getAll() {
        return listAll();
    }

    public int getNumOfGroups(Long tournamentId) {
        return (int) getEntityManager()
                .createQuery("select count(distinct p.groupNumber) from Phase p " +
                        "where p.groupNumber <> -1 " +
                        "and p.tournament.id = :tournamentId")
                .setParameter("tournamentId", tournamentId)
                .getSingleResult();
    }

    public Phase delete(Long id) {
        Phase phase = getById(id);
        nodeRepository.find("phase", phase).stream().forEach(p -> nodeRepository.delete(p));
        delete("id", id);
        return phase;
    }

    public long clear() {
        nodeRepository.clear();
        return deleteAll();
    }
}
