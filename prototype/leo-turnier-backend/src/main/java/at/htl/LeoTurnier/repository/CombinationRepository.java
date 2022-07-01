package at.htl.LeoTurnier.repository;


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


    public Tournament startGroupPhase(Tournament tournament, List<Competitor> competitors, int numOfGroups, int promotedPerGroup) {
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
}
