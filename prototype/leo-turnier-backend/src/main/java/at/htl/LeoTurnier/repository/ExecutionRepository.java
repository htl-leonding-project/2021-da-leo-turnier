package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ExecutionRepository {

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
    EliminationRepository eliminationRepository;

    @Inject
    RoundRobinRepository roundRobinRepository;

    @Inject
    CombinationRepository combinationRepository;

    public Tournament startTournament(Long tournamentId, Integer numOfGroups) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null || tournament.getTournamentMode() == null) {
            return null;
        }
        clearTournament(tournamentId);
        List<Competitor> competitors = competitorRepository.getByTournamentId(tournament.getId());
        if (tournament.getTournamentMode().getName().equals("Round Robin")) {
            tournament = roundRobinRepository.startTournament(tournament, competitors, -1);
        } else if (tournament.getTournamentMode().getName().equals("Combination") && numOfGroups != null) {
            tournament = combinationRepository.startGroupPhase(tournament, competitors, numOfGroups);
        } else {
            tournament = eliminationRepository.startTournament(tournament, competitors);
        }

        return tournament;
    }

    public Tournament startTieBreakers(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if(tournament == null || tournament.getTournamentMode() == null) {
            return null;
        }
        if (tournament.getTournamentMode().getName().equals("Round Robin")) {
            tournament = roundRobinRepository.startTieBreakers(tournament, -1);
        } else if (tournament.getTournamentMode().getName().equals("Combination")) {
            tournament = combinationRepository.startTieBreakers(tournament);
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

    public Match finishMatch(Long matchId) {
        Node node = nodeRepository.getByMatchId(matchId);
        node.getMatch().setFinished(true);
        matchRepository.modify(node.getMatch().getId(), node.getMatch());
        Tournament tournament = node.getPhase().getTournament();
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            roundRobinRepository.rankCompetitors(tournament, -1);
            roundRobinRepository.setFinished(tournament);
            return node.getMatch();
        } else if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Combination")) {
            if (node.getPhase().getGroupNumber() != -1) {
                roundRobinRepository.rankCompetitors(tournament, node.getPhase().getGroupNumber());
            } else {
                eliminationRepository.finishMatch(node, tournament);
            }
            return node.getMatch();
        } else {
            return eliminationRepository.finishMatch(node, tournament);
        }
    }

    public void rankCompetitors(Long tournamentId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Round Robin")) {
            roundRobinRepository.rankCompetitors(tournament, -1);
        } else if (tournament.getTournamentMode() != null && tournament.getTournamentMode().getName().equals("Combination")) {
             combinationRepository.rankCompetitors(tournament);
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
