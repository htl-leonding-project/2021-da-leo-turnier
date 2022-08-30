package at.htl.LeoTurnier.execution;


import at.htl.LeoTurnier.dto.CompetitorDto;
import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.repository.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class CombinationExecution {

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    RoundRobinExecution roundRobinExecution;

    @Inject
    EliminationExecution eliminationExecution;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    CompetitorRepository competitorRepository;


    public Tournament startGroupPhase(Tournament tournament, List<Competitor> competitors, int numOfGroups) {
        if (numOfGroups > competitors.size()) {
            return null;
        }
        eliminationExecution.sortBySeed(tournament, competitors);
        boolean x = false;
        for (int i = 0; i < numOfGroups; i++) {
            List<Competitor> competitorsInGroup = new LinkedList<>();
            for (int u = 0; u < competitors.size(); u++) {
                if (u % numOfGroups == 0) {
                    x = !x;
                }
                if (x) {
                    if ((u - i) % numOfGroups == 0) {
                        competitorsInGroup.add(competitors.get(u));
                    }
                } else {
                    if ((u - (numOfGroups - i - 1)) % numOfGroups == 0) {
                        competitorsInGroup.add(competitors.get(u));
                    }
                }
            }
            roundRobinExecution.startTournament(tournament, competitorsInGroup, i);
        }

        return tournament;
    }

    public Tournament startTieBreakers(Tournament tournament) {
        for (int i = 0; i < phaseRepository.getNumOfGroups(tournament.getId()); i++) {
            roundRobinExecution.startTieBreakers(tournament, i);
        }
        return tournament;
    }

    public Tournament startKOPhase(Tournament tournament, int promotedPerGroup) {
        List<Competitor> promoted = new LinkedList<>();
        for (int i = 0; i < phaseRepository.getNumOfGroups(tournament.getId()); i++) {
            List<CompetitorDto> competitorDtos = roundRobinExecution.getCompetitorsSorted(tournament, i);
            for (int u = 0; u < promotedPerGroup; u++) {
                promoted.add(competitorRepository.getById(competitorDtos.get(u).getId()));
            }
        }
        return eliminationExecution.startTournament(tournament, promoted);
    }

    public void rankCompetitors(Tournament tournament) {
        int numOfGroups = phaseRepository.getNumOfGroups(tournament.getId());
        for (int i = 0; i < numOfGroups; i++) {
            List<CompetitorDto> competitorDtos = roundRobinExecution.getCompetitorsSorted(tournament, i);
            for (int u = 0; u < competitorDtos.size(); u++) {
                CompetitorDto competitorDto = competitorDtos.get(u);
                CompetitorDto previousCompetitorDto = null;
                if (u > 0) {
                    previousCompetitorDto = competitorDtos.get(u - 1);
                }
                if (previousCompetitorDto != null &&
                        competitorDto.getWins() == previousCompetitorDto.getWins() &&
                        competitorDto.getPoints() == previousCompetitorDto.getPoints()) {
                    participationRepository.modifyPlacement(tournament.getId(),
                            competitorDtos.get(u).getId(),
                            participationRepository.getById(tournament.getId(),
                                    previousCompetitorDto.getId()).getPlacement());
                } else {
                    participationRepository.modifyPlacement(tournament.getId(), competitorDtos.get(u).getId(), (u + 1) * numOfGroups);
                }
            }
        }
    }
}
