package at.htl.LeoTurnier.repository;


import at.htl.LeoTurnier.dto.CompetitorDto;
import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Tournament;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class CombinationRepository {

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    RoundRobinRepository roundRobinRepository;

    @Inject
    EliminationRepository eliminationRepository;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    CompetitorRepository competitorRepository;


    public Tournament startGroupPhase(Tournament tournament, List<Competitor> competitors, int numOfGroups) {
        eliminationRepository.sortBySeed(competitors);

        for (int i = 0; i < numOfGroups; i++) {
            List<Competitor> competitorsInGroup = new LinkedList<>();
            for (int u = 0; u < competitors.size(); u++) {
                if ((u - i) % numOfGroups == 0) {
                    competitorsInGroup.add(competitors.get(u));
                }
            }
            roundRobinRepository.startTournament(tournament, competitorsInGroup, i);
        }

        return tournament;
    }

    public Tournament startKOPhase(Tournament tournament, int promotedPerGroup) {
        List<Competitor> promoted = new LinkedList<>();
        for (int i = 0; i < phaseRepository.getNumOfGroups(tournament.getId()); i++) {
            List<CompetitorDto> competitorDtos = roundRobinRepository.getCompetitorsSorted(tournament, i);
            for (int u = 0; u < promotedPerGroup; u++) {
                promoted.add(competitorRepository.getById(competitorDtos.get(u).getId()));
            }
        }
        return eliminationRepository.startTournament(tournament, promoted);
    }

    public void rankCompetitors(Tournament tournament, int groupNumber) {
        int numOfGroups = phaseRepository.getNumOfGroups(tournament.getId());
        List<CompetitorDto> competitorDtos = roundRobinRepository.getCompetitorsSorted(tournament, groupNumber);
        for (int i = 0; i < competitorDtos.size(); i++) {
            CompetitorDto competitorDto = competitorDtos.get(i);
            CompetitorDto previousCompetitorDto = null;
            if (i > 0) {
                previousCompetitorDto = competitorDtos.get(i - 1);
            }
            if (previousCompetitorDto != null &&
                    competitorDto.getWins() == previousCompetitorDto.getWins() &&
                    competitorDto.getPoints() == previousCompetitorDto.getPoints()) {
                participationRepository.modify(tournament.getId(),
                        competitorDtos.get(i).getId(),
                        participationRepository.getById(tournament.getId(),
                                previousCompetitorDto.getId()).getPlacement());
            } else {
                participationRepository.modify(tournament.getId(), competitorDtos.get(i).getId(), i + numOfGroups);
            }
        }
    }
}
