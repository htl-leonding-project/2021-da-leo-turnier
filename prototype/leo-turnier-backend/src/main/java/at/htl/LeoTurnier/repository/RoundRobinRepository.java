package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.dto.CompetitorDto;
import at.htl.LeoTurnier.entity.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoundRobinRepository {

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    CompetitorRepository competitorRepository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    TournamentRepository tournamentRepository;

    public Tournament startTournament(Tournament tournament, List<Competitor> competitors, int groupNumber) {
        insertPhasesRoundRobin(tournament, competitors, groupNumber, 0);
        insertNodesAndMatchesRoundRobin(tournament, competitors, groupNumber, 0);
        return tournament;
    }

    public Tournament startTieBreakers(Tournament tournament, int groupNumber) {
        List<CompetitorDto> competitorDtos = getCompetitorsSorted(tournament, groupNumber);
        List<CompetitorDto> standingsShared = new LinkedList<>();
        for (int i = 0; i < competitorDtos.size() - 1; i++) {
            CompetitorDto curr = competitorDtos.get(i);
            if (curr.equals(competitorDtos.get(i + 1)) &&
                    standingsShared.stream().noneMatch(c -> c.equals(curr))) {
                standingsShared.add(new CompetitorDto(curr.getWins(), curr.getPoints(), curr.getTieBreakerWins()));
            }
        }
        for (CompetitorDto standing : standingsShared) {
            List<Competitor> tiedCompetitors = competitorDtos.stream()
                    .filter(c -> c.equals(standing))
                    .map(c -> competitorRepository.getById(c.getId()))
                    .collect(Collectors.toList());
            int startingPhaseNumber = phaseRepository.getMaxPhaseNumberForGroup(tournament.getId(), groupNumber) + 1;
            insertPhasesRoundRobin(tournament, tiedCompetitors, groupNumber, startingPhaseNumber);
            insertNodesAndMatchesRoundRobin(tournament, tiedCompetitors, groupNumber, startingPhaseNumber);
        }
        return tournament;
    }

    public void insertPhasesRoundRobin(Tournament tournament, List<Competitor> competitors,
                                       int groupNumber, int startingPhaseNumber) {
        double numOfPhases = competitors.size() - 1 + (competitors.size() % 2);
        for (int i = 0; i < numOfPhases; i++) {
            phaseRepository.add(new Phase(i + startingPhaseNumber, groupNumber, tournament));
        }
    }

    public void insertNodesAndMatchesRoundRobin(Tournament tournament, List<Competitor> competitors,
                                                int groupNumber, int startingPhaseNumber) {
        List<Phase> phases = phaseRepository.getByTournamentGroup(tournament.getId(), groupNumber);
        int numOfNodes = competitors.size() / 2;
        for (int i = startingPhaseNumber; i < phases.size(); i++) {
            Phase phase = phases.get(i);
            List<Competitor> competitorsTmp = new ArrayList<>(competitors);
            if (competitorsTmp.size() % 2 != 0) {
                competitorsTmp.remove(i % competitorsTmp.size());
            }
            for (int u = 0; u < numOfNodes; u++) {
                Match match = new Match(competitorsTmp.remove(0),
                        competitorsTmp.remove(i % competitorsTmp.size()));
                if (startingPhaseNumber > 0) {
                    nodeRepository.add(new Node(-1, match, phase));
                } else {
                    nodeRepository.add(new Node(u, match, phase));
                }
            }
        }
    }

    public void rankCompetitors(Tournament tournament, int groupNumber) {
        List<CompetitorDto> competitorDtos = getCompetitorsSorted(tournament, groupNumber);
        for (int i = 0; i < competitorDtos.size(); i++) {
            CompetitorDto competitorDto = competitorDtos.get(i);
            CompetitorDto previousCompetitorDto = null;
            if (i > 0) {
                previousCompetitorDto = competitorDtos.get(i - 1);
            }
            if (previousCompetitorDto != null &&
                    competitorDto.getWins() == previousCompetitorDto.getWins() &&
                    competitorDto.getPoints() == previousCompetitorDto.getPoints()) {
                participationRepository.modifyPlacement(tournament.getId(),
                        competitorDtos.get(i).getId(),
                        participationRepository.getById(tournament.getId(),
                                previousCompetitorDto.getId()).getPlacement());
            } else {
                participationRepository.modifyPlacement(tournament.getId(), competitorDtos.get(i).getId(), i + 1);
            }
        }
    }

    public List<CompetitorDto> getCompetitorsSorted(Tournament tournament, int groupNumber) {
        List<Phase> phases = phaseRepository.getByTournamentGroup(tournament.getId(), groupNumber);
        List<CompetitorDto> competitorDtos = new LinkedList<>();
        phases.forEach(p -> {
            List<Node> nodes = nodeRepository.getByPhaseId(p.getId());
            nodes.forEach(n -> {
                Match match = n.getMatch();

                var optionalCompetitorDto1 = competitorDtos.stream()
                        .filter(c -> c.getId().equals(match.getCompetitor1().getId())).findAny();
                var optionalCompetitorDto2 = competitorDtos.stream()
                        .filter(c -> c.getId().equals(match.getCompetitor2().getId())).findAny();
                CompetitorDto competitorDto1;
                CompetitorDto competitorDto2;
                if (optionalCompetitorDto1.isPresent()) {
                    competitorDto1 = optionalCompetitorDto1.get();
                } else {
                    competitorDto1 = new CompetitorDto(match.getCompetitor1().getId());
                    competitorDtos.add(competitorDto1);
                }
                if (optionalCompetitorDto2.isPresent()) {
                    competitorDto2 = optionalCompetitorDto2.get();
                } else {
                    competitorDto2 = new CompetitorDto(match.getCompetitor2().getId());
                    competitorDtos.add(competitorDto2);
                }

                competitorDto1.addPoints(match.getScore1());
                competitorDto2.addPoints(match.getScore2());
                if (match.getScore1() > match.getScore2()) {
                    competitorDto1.addWin();
                } else if (match.getScore1() < match.getScore2()) {
                    competitorDto2.addWin();
                }
            });
        });

        competitorDtos.sort((c1, c2) -> {
            if (c1.getWins() > c2.getWins()) {
                return -1;
            } else if (c1.getWins() < c2.getWins()) {
                return 1;
            } else if (c1.getPoints() > c2.getPoints()) {
                return -1;
            } else if (c1.getPoints() < c2.getPoints()) {
                return 1;
            }
            return 0;
        });

        return competitorDtos;
    }

    public void setFinished(Tournament tournament) {
        boolean isFinished = true;
        List<Phase> phases = phaseRepository.getByTournamentId(tournament.getId());
        for (Phase p : phases) {
            List<Node> nodes = nodeRepository.getByPhaseId(p.getId());
            for (Node n : nodes) {
                if (!n.getMatch().isFinished()) {
                    isFinished = false;
                    break;
                }
            }
        }
        List<CompetitorDto> competitorDtos = getCompetitorsSorted(tournament, -1);
        for (int u = 0; u < competitorDtos.size() - 1; u++) {
            CompetitorDto curr = competitorDtos.get(u);
            if (curr.equals(competitorDtos.get(u + 1))) {
                isFinished = false;
                break;
            }
        }
        tournament.setFinished(isFinished);
        tournamentRepository.modify(tournament.getId(),tournament);
    }
}
