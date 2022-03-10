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
    private final List<Player> players = List.of(
            new Player("defaultPlayer1"),
            new Player("defaultPlayer2"),
            new Player("defaultPlayer3"),
            new Player("defaultPlayer4"),
            new Player("defaultPlayer5"),
            new Player("defaultPlayer6"),
            new Player("defaultPlayer7"),
            new Player("defaultPlayer8")
    );

    private void insertTestData() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setSeed(i + 1);
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
        insertTestData();
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
        insertTestData();
        // arrange
        Player player9 = new Player("Player9");
        Player player10 = new Player("Player10");
        Player player11 = new Player("Player11");
        competitorRepository.add(player9);
        competitorRepository.add(player10);
        competitorRepository.add(player11);
        participationRepository.add(defaultTournament1.getId(), player9.getId());
        participationRepository.add(defaultTournament1.getId(), player10.getId());
        participationRepository.add(defaultTournament1.getId(), player11.getId());

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
}