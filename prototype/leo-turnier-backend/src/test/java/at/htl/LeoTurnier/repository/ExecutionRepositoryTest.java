package at.htl.LeoTurnier.repository;


import at.htl.LeoTurnier.entity.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExecutionRepositoryTest {

    @Inject
    ExecutionRepository repository;

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    CompetitorRepository competitorRepository;

    @Inject
    ParticipationRepository participationRepository;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    NodeRepository nodeRepository;

    private final Tournament defaultTournament1 = new Tournament("defaultTournament1");

    private void insertTestData(int numOfPlayers) {
        List<Player> players = new LinkedList<>();
        for (int i = 0; i < numOfPlayers; i++) {
            Player player = new Player(String.valueOf(i));
            player.setSeed(i + 1);
            players.add(player);
        }
        tournamentRepository.add(defaultTournament1);
        players.forEach(p -> {
            competitorRepository.add(p);
            participationRepository.add(defaultTournament1.getId(), p.getId());
        });
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        tournamentRepository.clear();
        competitorRepository.clear();
        participationRepository.clear();
        phaseRepository.clear();
        nodeRepository.clear();
    }

    @Test
    @Order(1010)
    void TestStartTournament01_TournamentWith8Players_ShouldSetUpTournament() {
        insertTestData(8);
        // arrange

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId());

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(3);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(7);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(4);
    }

    @Test
    @Order(1020)
    void TestStartTournament02_TournamentWith11Players_ShouldSetUpTournament() {
        insertTestData(11);
        // arrange

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId());

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(4);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(10);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(8);
    }

    @Test
    @Order(1030)
    void TestStartTournament03_TournamentWith100Players_ShouldSetUpTournament() {
        insertTestData(100);
        // arrange

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId());

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(7);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(99);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(64);
    }
}