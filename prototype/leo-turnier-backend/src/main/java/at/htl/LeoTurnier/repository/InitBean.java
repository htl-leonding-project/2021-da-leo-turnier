package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.TournamentMode;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class InitBean {

    @Inject
    TournamentModeRepository tournamentModeRepository;

    void onStartup(@Observes StartupEvent event) {
        fillTournamentModes();
    }

    @Transactional
    public void fillTournamentModes() {
        tournamentModeRepository.add(new TournamentMode("Elimination"));
        tournamentModeRepository.add(new TournamentMode("Round Robin"));
        tournamentModeRepository.add(new TournamentMode("Combination"));
    }
}
