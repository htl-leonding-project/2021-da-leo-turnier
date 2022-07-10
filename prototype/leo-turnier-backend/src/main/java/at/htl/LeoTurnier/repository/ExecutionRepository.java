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

    @Inject
    EliminationRepository eliminationRepository;

    @Inject
    RoundRobinRepository roundRobinRepository;

    @Inject
    CombinationRepository combinationRepository;

    public Tournament startTournament(Long tournamentId, Integer numOfGroups) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null) {
            return null;
        }
        clearTournament(tournament);
        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        if (tournament.getTournamentMode().getName().equals("Round Robin")) {
            tournament = roundRobinRepository.startTournament(tournament, competitors, -1);
        } else if (tournament.getTournamentMode().getName().equals("Combination") && numOfGroups != null) {
            tournament = combinationRepository.startGroupPhase(tournament, competitors, numOfGroups);
        } else {
            tournament = eliminationRepository.startTournament(tournament, competitors);
        }

        return tournament;
    }

    public Tournament startKOPhase(Long tournamentId, Integer promotedPerGroup) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null || tournament.getTournamentMode() == null) {
            return null;
        }
        if (tournament.getTournamentMode().getName().equals("Combination") && promotedPerGroup != -1) {
            return combinationRepository.startKOPhase(tournament, promotedPerGroup);
        }
        return null;
    }

    public Match finishMatch(Long nodeId) {
        Node node = nodeRepository.getById(nodeId);
        Tournament tournament = node.getPhase().getTournament();
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            roundRobinRepository.rankCompetitors(tournament);
            return node.getMatch();
        } else if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Combination")) {
            if (node.getPhase().getGroupNumber() != -1) {
                combinationRepository.rankCompetitors(tournament, node.getPhase().getGroupNumber());
            } else {
                eliminationRepository.finishMatch(node, tournament);
            }
            return node.getMatch();
        } else {
            return eliminationRepository.finishMatch(node, tournament);
        }
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
