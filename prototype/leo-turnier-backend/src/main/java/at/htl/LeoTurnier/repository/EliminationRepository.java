package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EliminationRepository {

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    MatchRepository matchRepository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    PhaseRepository phaseRepository;

    public Match finishMatch(Node node, Tournament tournament) {
        Match match = node.getMatch();
        Competitor winner;
        Competitor loser;
        if (match.getScore1() > match.getScore2()) {
            winner = match.getCompetitor1();
            loser = match.getCompetitor2();
        } else if (match.getScore1() < match.getScore2()) {
            winner = match.getCompetitor2();
            loser = match.getCompetitor1();
        } else {
            return null;
        }

        if (node.getNextNode() != null) {
            participationRepository.modify(tournament.getId(),
                    loser.getId(),
                    nodeRepository.getByPhaseId(node.getNextNode().getPhase().getId()).size() * 2 + 1);
        } else {
            participationRepository.modify(tournament.getId(),
                    loser.getId(),
                    2);
            participationRepository.modify(tournament.getId(),
                    winner.getId(),
                    1);
        }

        Match nextMatch = node.getNextNode().getMatch();
        if (nextMatch == null) {
            nextMatch = matchRepository.add(new Match());
        }
        if (nextMatch.getCompetitor1() == null) {
            nextMatch.setCompetitor1(winner);
        } else if (nextMatch.getCompetitor2() == null) {
            nextMatch.setCompetitor2(winner);
        }
        matchRepository.modify(nextMatch.getId(), nextMatch);
        return nextMatch;
    }

    public void insertPhasesElimination(Tournament tournament) {
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        double numOfPhases = (Math.log(competitors.size())
                /  Math.log(2));
        // numOfPlayers = 2^numOfPhases
        // solve for phases -> numOfPhases = log2(numOfCompetitors) = log(numOfCompetitors) / log(2)
        for (int i = 0; i < numOfPhases; i++) {
            phaseRepository.add(new Phase(i, tournament));
        }
    }

    public void insertNodesElimination(Tournament tournament) {
        List<Phase> phases = phaseRepository.getByTournamentId(tournament.getId());
        List<Node> previousNodes = null;

        for (int i = 0; i < phases.size(); i++) {
            Phase phase = phases.get(phases.size() - 1 - i);
            int numOfNodes = (int) Math.pow(2, i);
            for (int u = 0; u < numOfNodes; u++) {
                Node node = new Node(u, phase);
                if (previousNodes != null) {
                    node.setNextNode(previousNodes.get(u / 2));
                }
                nodeRepository.add(node);
            }
            previousNodes = nodeRepository.getByPhaseId(phase.getId());
        }
    }

    public void insertMatchesElimination(Tournament tournament) {
        List<Phase> phases = phaseRepository.getByTournamentId(tournament.getId());
        if (phases.size() <= 1) {
            return;
        }
        List<Competitor> competitors = getCompetitorsSeeded(tournament);
        List<Node> nodes = nodeRepository.getByPhaseId(phases.get(0).getId());
        for (int i = 0; i < competitors.size(); i++) {
            Node node = nodes.get(i / 2);
            if (node.getMatch() == null) {
                Match match = matchRepository.add(new Match());
                node.setMatch(match);
                nodeRepository.modify(node.getId(), node);
            }
            if (node.getMatch().getCompetitor1() == null) {
                node.getMatch().setCompetitor1(competitors.get(i));
            } else if (node.getMatch().getCompetitor2() == null) {
                node.getMatch().setCompetitor2(competitors.get(i));
            }
            matchRepository.modify(node.getMatch().getId(), node.getMatch());
        }
    }

    public void setBuyRoundsElimination(Tournament tournament) {
        Phase phase = phaseRepository.getByTournamentId(tournament.getId()).get(0);
        List<Node> nodes = nodeRepository.getByPhaseId(phase.getId());
        nodes.forEach(n -> {
            if (n.getMatch().getCompetitor1() == null || n.getMatch().getCompetitor2() == null) {
                Node nextNode = n.getNextNode();
                nextNode.setMatch(n.getMatch());
                nodeRepository.delete(n.getId());
                nodeRepository.modify(nextNode.getId(), nextNode);
            }
        });
    }

    public List<Competitor> getCompetitorsSeeded(Tournament tournament) {
        // sort by seed
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        competitors.sort((c1, c2) -> {
                    if (c1.getSeed() >= 0 && c2.getSeed() < 0) {
                        return -1;
                    } else if (c1.getSeed() < 0 && c2.getSeed() >= 0) {
                        return 1;
                    } else if (c1.getSeed() < c2.getSeed()) {
                        return -1;
                    } else if (c1.getSeed() > c2.getSeed()) {
                        return 1;
                    }
                    return 0;
                });

        // add dummies
        int numOfPhases = phaseRepository.getByTournamentId(tournament.getId()).size();
        int numOfCompetitors = competitors.size();
        for (int i = 0; i < Math.pow(2, numOfPhases) - numOfCompetitors; i++) {
            competitors.add(null);
        }

        // seed competitors
        competitors = seedCompetitors(competitors);

        return competitors;
    }

    public List<Competitor> seedCompetitors(List<Competitor> competitors) {
        if ((Math.log(competitors.size()) /  Math.log(2)) % 1 != 0 && competitors.size() > 1) { // if numOfCompetitors is an exponent of 2
            return competitors;
        }
        return seedCompetitors(competitors, 1);
    }

    public List<Competitor> seedCompetitors(List<Competitor> competitors, int level) {
        level = level * 2;
        int numOfCompetitors = competitors.size();
        List<Competitor> res = new LinkedList<>();
        for (int i = 0; i < numOfCompetitors / level; i++) {
            for (int u = 0; u < level / 2; u++) {
                res.add(competitors.get(0));
                competitors.remove(0);
            }
            for (int u = 0; u < level / 2; u++) {
                res.add(competitors.get(competitors.size() - 1));
                competitors.remove(competitors.size() - 1);
            }
        }
        if (level < numOfCompetitors) {
            res = seedCompetitors(res, level);
        }
        return res;
    }
}
