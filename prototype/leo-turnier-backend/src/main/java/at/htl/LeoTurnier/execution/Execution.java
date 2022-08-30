package at.htl.LeoTurnier.execution;

import at.htl.LeoTurnier.entity.*;
import at.htl.LeoTurnier.repository.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class Execution {

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    CompetitorRepository competitorRepository;

    @Inject
    MatchRepository matchRepository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    EliminationExecution eliminationExecution;

    @Inject
    RoundRobinExecution roundRobinExecution;

    @Inject
    CombinationExecution combinationExecution;

    public Tournament startTournament(Long tournamentId, Integer numOfGroups) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null || tournament.getTournamentMode() == null) {
            return null;
        }
        clearTournament(tournamentId);
        List<Competitor> competitors = competitorRepository.getByTournamentId(tournament.getId());
        if (tournament.getTournamentMode().getName().equals("Round Robin")) {
            tournament = roundRobinExecution.startTournament(tournament, competitors, -1);
        } else if (tournament.getTournamentMode().getName().equals("Combination") && numOfGroups != null) {
            tournament = combinationExecution.startGroupPhase(tournament, competitors, numOfGroups);
        } else {
            tournament = eliminationExecution.startTournament(tournament, competitors);
        }

        return tournament;
    }

    public Tournament startTieBreakers(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null || tournament.getTournamentMode() == null) {
            return null;
        }
        if (tournament.getTournamentMode().getName().equals("Round Robin")) {
            tournament = roundRobinExecution.startTieBreakers(tournament, -1);
        } else if (tournament.getTournamentMode().getName().equals("Combination")) {
            tournament = combinationExecution.startTieBreakers(tournament);
        }
        return tournament;
    }

    public Tournament startKOPhase(Long tournamentId, Integer promotedPerGroup) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null || tournament.getTournamentMode() == null) {
            return null;
        }
        if (tournament.getTournamentMode().getName().equals("Combination") && promotedPerGroup != -1) {
            return combinationExecution.startKOPhase(tournament, promotedPerGroup);
        }
        return null;
    }

    public Match finishMatch(Long matchId) {
        Node node = nodeRepository.getByMatchId(matchId);
        node.getMatch().setFinished(true);
        matchRepository.modify(node.getMatch().getId(), node.getMatch());
        Tournament tournament = node.getPhase().getTournament();
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            roundRobinExecution.rankCompetitors(tournament, -1);
            roundRobinExecution.setFinished(tournament);
            return node.getMatch();
        } else if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Combination")) {
            if (node.getPhase().getGroupNumber() != -1) {
                roundRobinExecution.rankCompetitors(tournament, node.getPhase().getGroupNumber());
            } else {
                eliminationExecution.finishMatch(node, tournament);
            }
            return node.getMatch();
        } else {
            return eliminationExecution.finishMatch(node, tournament);
        }
    }

    public void rankCompetitors(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            roundRobinExecution.rankCompetitors(tournament, -1);
        } else if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Combination")) {
             combinationExecution.rankCompetitors(tournament);
        }
    }

    public void clearTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null) {
            return;
        }
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
