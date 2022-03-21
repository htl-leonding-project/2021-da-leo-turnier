package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.callback.TextOutputCallback;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExecutionRepository {

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    MatchRepository matchRepository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    PhaseRepository phaseRepository;

    public Tournament startTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null) {
            return null;
        }
        clearTournament(tournament);
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            insertPhasesRoundRobin(tournament);
            insertNodesAndMatchesRoundRobin(tournament);
        } else {
            insertPhasesElimination(tournament);
            insertNodesElimination(tournament);
            insertMatchesElimination(tournament);
            setBuyRoundsElimination(tournament);
        }

        return tournament;
    }

    public Match finishMatch(Long nodeId) {
        Node node = nodeRepository.getById(nodeId);
        Tournament tournament = node.getPhase().getTournament();
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            return null;
        }
        if (node.getNextNode() == null) {
            return null;
        }
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

    private void insertPhasesRoundRobin(Tournament tournament) {
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        double numOfPhases = competitors.size() - 1 + (competitors.size() % 2);
        for (int i = 0; i < numOfPhases; i++) {
            phaseRepository.add(new Phase(i, tournament));
        }
    }

    private void insertNodesAndMatchesRoundRobin(Tournament tournament) {
        List<Phase> phases = phaseRepository.getByTournamentId(tournament.getId());
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        int numOfNodes = competitors.size() / 2;
        for (int i = 0; i < phases.size(); i++) {
            Phase phase = phases.get(i);
            List<Competitor> competitorsTmp = new ArrayList<>(competitors);
            if (competitorsTmp.size() % 2 != 0) {
                competitorsTmp.remove(i % competitorsTmp.size());
            }
            for (int u = 0; u < numOfNodes; u++) {
                Match match = new Match(competitorsTmp.remove(0),
                        competitorsTmp.remove(i % competitorsTmp.size()));
                nodeRepository.add(new Node(u, match, phase));
            }
        }
    }

    private void insertPhasesElimination(Tournament tournament) {
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        double numOfPhases = (Math.log(competitors.size())
                 /  Math.log(2));
        // numOfPlayers = 2^numOfPhases
        // solve for phases -> numOfPhases = log2(numOfCompetitors) = log(numOfCompetitors) / log(2)
        for (int i = 0; i < numOfPhases; i++) {
            phaseRepository.add(new Phase(i, tournament));
        }
    }

    private void insertNodesElimination(Tournament tournament) {
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

    private void insertMatchesElimination(Tournament tournament) {
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

    private void setBuyRoundsElimination(Tournament tournament) {
        Phase phase = phaseRepository.getByTournamentId(tournament.getId()).get(0);
        List<Node> nodes = nodeRepository.getByPhaseId(phase.getId());
        nodes.forEach(n -> {
            if (n.getMatch().getCompetitor1() == null || n.getMatch().getCompetitor2() == null) {
                Node nextNode = n.getNextNode();
                nextNode.setMatch(n.getMatch());
                nodeRepository.delete(n.getId());
                nodeRepository.modify(nextNode.getId(), nextNode.getNextNode());
            }
        });
    }

    private List<Competitor> getCompetitorsSeeded(Tournament tournament) {
        // sort by seed
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId()).parallelStream()
                .sorted((c1, c2) -> {
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
                }).collect(Collectors.toList());

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

    private List<Competitor> seedCompetitors(List<Competitor> competitors) {
        if ((Math.log(competitors.size()) /  Math.log(2)) % 1 != 0 && competitors.size() > 1) { // if numOfCompetitors is an exponent of 2
            return competitors;
        }
        return seedCompetitors(competitors, 1);
    }

    private List<Competitor> seedCompetitors(List<Competitor> competitors, int level) {
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

    private void clearTournament(Tournament tournament) {
        List<Phase> phases = phaseRepository.getByTournamentId(tournament.getId());
        phases.forEach(p -> {
            List<Node> nodes = nodeRepository.getByPhaseId(p.getId());
            nodes.forEach(n -> {
                matchRepository.delete(n.getMatch().getId());
                nodeRepository.delete(n.getId());
            });
            phaseRepository.delete(p.getId());
        });
    }
}
