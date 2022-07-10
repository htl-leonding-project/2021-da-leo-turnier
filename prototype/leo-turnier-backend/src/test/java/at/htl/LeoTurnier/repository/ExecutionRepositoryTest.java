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
            Player player = new Player(String.valueOf(i + 1));
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
    }

    @Test
    @Order(1010)
    void TestEliminationStartTournament01_TournamentWith8Players_ShouldSetUpTournament() {
        insertTestData(8);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Elimination"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), null);

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
    void TestEliminationStartTournament02_TournamentWith11Players_ShouldSetUpTournament() {
        insertTestData(11);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Elimination"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), null);

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
    void TestEliminationStartTournament03_TournamentWith100Players_ShouldSetUpTournament() {
        insertTestData(100);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Elimination"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), null);

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

    @Test
    @Order(2010)
    void StartRoundRobinTestTournament01_TournamentWith4Players_ShouldSetUpTournament() {
        insertTestData(4);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Round Robin"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), null);

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(3);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(6);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(6);
    }

    @Test
    @Order(2020)
    void StartRoundRobinTestTournament02_TournamentWith5Players_ShouldSetUpTournament() {
        insertTestData(5);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Round Robin"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), null);

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(5);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(10);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(10);
    }

    @Test
    @Order(2030)
    void StartRoundRobinTestTournament03_TournamentWith8Players_ShouldSetUpTournament() {
        insertTestData(8);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Round Robin"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), null);

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(7);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(28);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(28);
    }

    @Test
    @Order(3010)
    void TestCombinationStartTournament01_TournamentWith16Players_ShouldSetUpTournament() {
        insertTestData(16);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Combination"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), 4);

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(12);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(24);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(24);
    }

    @Test
    @Order(3020)
    void TestCombinationStartTournament02_TournamentWith12Players_ShouldSetUpTournament() {
        insertTestData(12);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Combination"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), 4);

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(12);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(12);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(12);
    }

    @Test
    @Order(3030)
    void TestCombinationStartTournament03_TournamentWith19Players_ShouldSetUpTournament() {
        insertTestData(19);
        // arrange
        defaultTournament1.setTournamentMode(new TournamentMode("Combination"));
        tournamentRepository.modify(defaultTournament1.getId(), defaultTournament1);

        // act
        Tournament res = repository.startTournament(defaultTournament1.getId(), 4);

        // assert
        TypedQuery<Phase> getPhases = phaseRepository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Node> getNodes = nodeRepository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = nodeRepository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(18);
        assertThat(getNodes.getResultList().size())
                .isEqualTo(36);
        assertThat(getMatches.getResultList().size())
                .isEqualTo(36);
    }
}