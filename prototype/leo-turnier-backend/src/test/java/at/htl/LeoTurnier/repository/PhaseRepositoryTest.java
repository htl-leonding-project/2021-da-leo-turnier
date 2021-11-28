package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PhaseRepositoryTest {

    @Inject
    PhaseRepository repository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    TournamentRepository tournamentRepository;

    private final Tournament defaultTournament1 = new Tournament("MSI");
    private final Phase defaultPhase1 = new Phase(1, defaultTournament1);
    private final Node defaultNode1 = new Node(new Match(), defaultPhase1);

    private void insertTestData() {
        repository.add(defaultPhase1);
        nodeRepository.add(defaultNode1);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.clear();
        nodeRepository.clear();
        tournamentRepository.clear();
    }

    @Test
    @Order(1010)
    void TestAdd01_PassNull_ShouldReturnNull() {
        // arrange

        // act
        Phase res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddPhase_ShouldAddPhase() {
        // arrange
        int numberPhase1 = 2;
        Phase phase1 = new Phase(numberPhase1);

        // act
        repository.add(phase1);

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
        assertThat(getPhases.getResultList().get(0).getPhaseNumber())
                .isEqualTo(numberPhase1);
    }

    @Test
    @Order(1030)
    void TestAdd03_AddPhaseWithExistingTournament_ShouldAddPhase() {
        // arrange
        String nameTournament1 = "Worlds";
        int numberPhase1 = 2;
        Tournament tournament1 = new Tournament(nameTournament1);
        Phase phase1 = new Phase(numberPhase1, tournament1);

        // act
        tournamentRepository.add(tournament1);
        repository.add(phase1);

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
        assertThat(getPhases.getResultList().get(0).getPhaseNumber())
                .isEqualTo(numberPhase1);
        assertThat(getPhases.getResultList().get(0).getTournament().getName())
                .isEqualTo(nameTournament1);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);
    }

    @Test
    @Order(1040)
    void TestAdd04_AddPhaseWithNotExistingTournament_ShouldAddPhase() {
        // arrange
        String nameTournament1 = "Worlds";
        int numberPhase1 = 2;
        Tournament tournament1 = new Tournament(nameTournament1);
        Phase phase1 = new Phase(numberPhase1, tournament1);

        // act
        repository.add(phase1);

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
        assertThat(getPhases.getResultList().get(0).getPhaseNumber())
                .isEqualTo(numberPhase1);
        assertThat(getPhases.getResultList().get(0).getTournament().getName())
                .isEqualTo(nameTournament1);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Phase res = repository.modify(defaultPhase1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingPhase_ShouldReturnNull() {
        insertTestData();
        // arrange
        int numberPhase1 = 2;
        Phase phase1 = new Phase(numberPhase1);

        // act
        Phase res = repository.modify(-1, phase1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyPhase_ShouldModifyPhase() {
        insertTestData();
        // arrange
        int numberPhase1 = 2;
        Phase phase1 = new Phase(numberPhase1);

        // act
        repository.modify(defaultPhase1.getId(), phase1);

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
        assertThat(getPhases.getResultList().get(0).getPhaseNumber())
                .isEqualTo(numberPhase1);
    }

    @Test
    @Order(2040)
    void TestModify04_ModifyPhaseWithExistingTournament_ShouldModifyPhase() {
        insertTestData();
        // arrange
        int numberPhase1 = 2;
        Phase phase1 = new Phase(numberPhase1, defaultTournament1);

        // act
        repository.modify(defaultPhase1.getId(), phase1);

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
        assertThat(getPhases.getResultList().get(0).getPhaseNumber())
                .isEqualTo(numberPhase1);
        assertThat(getPhases.getResultList().get(0).getTournament().getName())
                .isEqualTo(defaultTournament1.getName());

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());
    }

    @Test
    @Order(2050)
    void TestModify05_ModifyPhaseWithNotExistingTournament_ShouldModifyPhase() {
        insertTestData();
        // arrange
        String nameTournament1 = "Worlds";
        int numberPhase1 = 2;
        Tournament tournament1 = new Tournament(nameTournament1);
        Phase phase1 = new Phase(numberPhase1, tournament1);

        // act
        repository.modify(defaultPhase1.getId(), phase1);

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
        assertThat(getPhases.getResultList().get(0).getPhaseNumber())
                .isEqualTo(numberPhase1);
        assertThat(getPhases.getResultList().get(0).getTournament().getName())
                .isEqualTo(tournament1.getName());

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(2);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());
        assertThat(getTournaments.getResultList().get(1).getName())
                .isEqualTo(tournament1.getName());
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingPhase_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Phase res = repository.getById((long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchPhase_ShouldReturnPhase() {
        insertTestData();
        // arrange

        // act
        Phase res = repository.getById(defaultPhase1.getId());

        // assert
        assertThat(res.getPhaseNumber())
                .isEqualTo(defaultPhase1.getPhaseNumber());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<Phase> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(0);
    }

    @Test
    @Order(4020)
    void TestGetAll02_SearchAll_ShouldReturnAll() {
        insertTestData();
        // arrange

        // act
        List<Phase> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(1);
        assertThat(res.get(0).getPhaseNumber())
                .isEqualTo(defaultPhase1.getPhaseNumber());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingPhase_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Phase res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeletePhase_ShouldDeletePhase() {
        insertTestData();
        // arrange

        // act
        Phase res = repository.delete(defaultPhase1.getId());

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(0);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());

        assertThat(getNodes.getResultList().size())
                .isEqualTo(0);

        assertThat(res.getPhaseNumber())
                .isEqualTo(defaultPhase1.getPhaseNumber());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<Node> getNode = repository.getEntityManager().createQuery("select n from Node n", Node.class);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(0);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());

        assertThat(getNode.getResultList().size())
                .isEqualTo(0);

        assertThat(res)
                .isEqualTo(1);
    }
}