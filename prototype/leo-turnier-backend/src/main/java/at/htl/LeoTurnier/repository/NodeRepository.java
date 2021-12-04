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
        if (node == null) {
            return null;
        }
        Node existing = getById(node.getId());
        if (existing != null) {
            return existing;
        }
        matchRepository.add(node.getMatch());
        phaseRepository.add(node.getPhase());
        persist(node);
        return node;
    }

    public Node modify(long id, Node node) {
        Node toModify = getById(id);
        if (node == null) {
            return null;
        }
        if (toModify != null) {
            matchRepository.add(node.getMatch());
            phaseRepository.add(node.getPhase());
            toModify.setMatch(node.getMatch());
            toModify.setPhase(node.getPhase());
        }
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
