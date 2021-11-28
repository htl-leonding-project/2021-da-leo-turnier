package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Node;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class NodeRepository implements PanacheRepository<Node> {

    @Inject
    MatchRepository matchRepository;

    @Inject
    PhaseRepository phaseRepository;

    public Node add(Node node) {
        if (node == null || getById(node.getId()) != null) {
            return null;
        }
        if (node.getMatch() != null) {
            matchRepository.add(node.getMatch());
            node.setMatch(matchRepository.getById(node.getMatch().getId()));
        }
        if (node.getPhase() != null) {
            phaseRepository.add(node.getPhase());
            node.setPhase(phaseRepository.getById(node.getPhase().getId()));
        }
        persist(node);
        return node;
    }

    public Node modify(long id, Node node) {
        Node toModify = getById(id);
        if (node == null || toModify == null) {
            return null;
        }
        if (node.getMatch() != null) {
            matchRepository.add(node.getMatch());
            node.setMatch(matchRepository.getById(node.getMatch().getId()));
        }
        if (node.getPhase() != null) {
            phaseRepository.add(node.getPhase());
            node.setPhase(phaseRepository.getById(node.getPhase().getId()));
        }
        toModify.setMatch(node.getMatch());
        toModify.setPhase(node.getPhase());
        return toModify;
    }

    public Node getById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Node> getAll() {
        return listAll();
    }

    public Node delete(Long id) {
        Node node = getById(id);
        delete("id", id);
        return node;
    }

    public long clear() {
        return deleteAll();
    }
}
