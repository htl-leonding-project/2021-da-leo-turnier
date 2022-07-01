package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoundRobinRepository {

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    PhaseRepository phaseRepository;

    public Tournament startTournament(Tournament tournament, List<Competitor> competitors, int groupNumber) {
        insertPhasesRoundRobin(tournament, competitors, groupNumber);
        insertNodesAndMatchesRoundRobin(tournament, competitors, groupNumber);
        return tournament;
    }

    public void insertPhasesRoundRobin(Tournament tournament, List<Competitor> competitors, int groupNumber) {
        double numOfPhases = competitors.size() - 1 + (competitors.size() % 2);
        for (int i = 0; i < numOfPhases; i++) {
            phaseRepository.add(new Phase(i, groupNumber, tournament));
        }
    }

    public void insertNodesAndMatchesRoundRobin(Tournament tournament, List<Competitor> competitors, int groupNumber) {
        List<Phase> phases = phaseRepository.getByTournamentGroup(tournament.getId(), groupNumber);
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

    public void finishTournament(Tournament tournament) {
        List<Phase> phases = phaseRepository.getByTournamentId(tournament.getId());
        Map<Long, Integer> wins = new HashMap<>();
        Map<Long, Integer> scores = new HashMap<>();

        List<Competitor> competitors = participationRepository.getCompetitorsByTournament(tournament.getId());
        competitors.forEach(c -> {
            wins.put(c.getId(), 0);
            scores.put(c.getId(), 0);
        });

        phases.forEach(p -> {
            List<Node> nodes = nodeRepository.getByPhaseId(p.getId());
            nodes.forEach(n -> {
                Match match = n.getMatch();
                Competitor winner = null;
                if (match.getScore1() > match.getScore2()) {
                    winner = match.getCompetitor1();
                } else if (match.getScore1() < match.getScore2()) {
                    winner = match.getCompetitor2();
                }
                if (winner != null) {
                    wins.put(winner.getId(), wins.get(winner.getId()) + 1);
                }
                scores.put(match.getCompetitor1().getId(),
                        scores.get(match.getCompetitor1().getId()) + match.getScore1());
                scores.put(match.getCompetitor2().getId(),
                        scores.get(match.getCompetitor2().getId()) + match.getScore2());
            });
        });

        competitors.sort((c1, c2) -> {
            if (wins.get(c1.getId()) > wins.get(c2.getId())) {
                return 1;
            }
            else if (wins.get(c1.getId()) < wins.get(c2.getId())) {
                return -1;
            }
            else if (scores.get(c1.getId()) > scores.get(c2.getId())) {
                return 1;
            }
            else if (scores.get(c1.getId()) < scores.get(c2.getId())) {
                return -1;
            }
            return 0;
        });

        for (int i = 0; i < competitors.size(); i++) {
            int placement = i;
            if (i > 0
            && Objects.equals(wins.get(competitors.get(i - 1).getId()),
                    wins.get(competitors.get(i).getId()))
            && Objects.equals(scores.get(competitors.get(i - 1).getId()),
                    scores.get(competitors.get(i).getId()))) {
                placement = participationRepository.getById(tournament.getId(),
                        competitors.get(i - 1).getId())
                        .getPlacement();
            }
            participationRepository.modify(tournament.getId(), competitors.get(i).getId(), placement);
        }
    }
}
