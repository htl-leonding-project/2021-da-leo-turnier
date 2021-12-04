package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NodeRepositoryTest {

    @Inject
    NodeRepository repository;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    MatchRepository matchRepository;

    private final Phase defaultPhase1 = new Phase(1);
    private final Match defaultMatch1 = new Match();
    private final Node defaultNode1 = new Node(defaultMatch1, defaultPhase1);

    private void insertTestData() {
        repository.add(defaultNode1);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.clear();
        phaseRepository.clear();
        matchRepository.clear();
    }

    @Test
    @Order(1010)
    void TestAdd01_PassNull_ShouldReturnNull() {
        // arrange

        // act
        Node res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddNode_ShouldAddNode() {
        // arrange
        Node node1 = new Node();

        // act
        repository.add(node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(1030)
    void TestAdd03_AddNodeWithExistingMatch_ShouldAddNode() {
        // arrange
        Match match1 = new Match();
        Node node1 = new Node(match1);

        // act
        matchRepository.add(match1);
        repository.add(node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getMatch().getId())
                .isEqualTo(match1.getId());

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(1040)
    void TestAdd04_AddNodeWithNotExistingMatch_ShouldAddNode() {
        // arrange
        Match match1 = new Match();
        Node node1 = new Node(match1);

        // act
        repository.add(node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getMatch().getId())
                .isEqualTo(match1.getId());

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(1050)
    void TestAdd05_AddNodeWithExistingPhase_ShouldAddNode() {
        // arrange
        Phase phase1 = new Phase();
        Node node1 = new Node(phase1);

        // act
        phaseRepository.add(phase1);
        repository.add(node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getPhase().getId())
                .isEqualTo(phase1.getId());

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(1060)
    void TestAdd06_AddNodeWithNotExistingPhase_ShouldAddNode() {
        // arrange
        Phase phase1 = new Phase();
        Node node1 = new Node(phase1);

        // act
        repository.add(node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getPhase().getId())
                .isEqualTo(phase1.getId());

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(1070)
    void TestAdd07_AddExistingNode_ShouldReturnExistingNode() {
        // arrange
        Node node1 = new Node();

        // act
        repository.add(node1);
        Node res = repository.add(node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);

        assertThat(res.getId())
                .isEqualTo(node1.getId());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Node res = repository.modify(defaultPhase1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingNode_ShouldReturnNull() {
        insertTestData();
        // arrange
        Node node1 = new Node();

        // act
        Node res = repository.modify(-1, node1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyNode_ShouldModifyNode() {
        insertTestData();
        // arrange
        Node node1 = new Node();

        // act
        repository.modify(defaultNode1.getId(), node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(2040)
    void TestModify04_ModifyNodeWithExistingMatch_ShouldModifyNode() {
        insertTestData();
        // arrange
        Node node1 = new Node(defaultMatch1);

        // act
        repository.modify(defaultNode1.getId(), node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getMatch().getId())
                .isEqualTo(defaultMatch1.getId());

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(2050)
    void TestModify05_ModifyNodeWithNotExistingMatch_ShouldModifyNode() {
        insertTestData();
        // arrange
        Match match1 = new Match();
        Node node1 = new Node(match1);

        // act
        repository.modify(defaultNode1.getId(), node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getMatch().getId())
                .isEqualTo(match1.getId());

        assertThat(getMatches.getResultList().size())
                .isEqualTo(2);
    }

    @Test
    @Order(2060)
    void TestModify06_ModifyNodeWithExistingPhase_ShouldModifyNode() {
        insertTestData();
        // arrange
        Node node1 = new Node(defaultPhase1);

        // act
        repository.modify(defaultNode1.getId(), node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getPhase().getId())
                .isEqualTo(defaultPhase1.getId());

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(2070)
    void TestModify07_ModifyNodeWithNotExistingPhase_ShouldModifyNode() {
        insertTestData();
        // arrange
        Phase phase1 = new Phase();
        Node node1 = new Node(phase1);

        // act
        repository.modify(defaultNode1.getId(), node1);

        // assert
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getNodes.getResultList().size())
                .isEqualTo(1);
        assertThat(getNodes.getResultList().get(0).getPhase().getId())
                .isEqualTo(phase1.getId());

        assertThat(getPhases.getResultList().size())
                .isEqualTo(2);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingNode_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Node res = repository.getById((long) -1);

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
        Node res = repository.getById(defaultNode1.getId());

        // assert
        assertThat(res)
                .isNotNull();
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<Node> res = repository.getAll();

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
        List<Node> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(1);
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingNode_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Node res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeleteNode_ShouldDeleteNode() {
        insertTestData();
        // arrange

        // act
        Node res = repository.delete(defaultNode1.getId());

        // assert
        TypedQuery<Node> getNode = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getNode.getResultList().size())
                .isEqualTo(0);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);

        assertThat(res)
                .isNotNull();
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<Node> getNode = repository.getEntityManager().createQuery("select n from Node n", Node.class);
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Phase> getPhases = repository.getEntityManager().createQuery("select p from Phase p", Phase.class);

        assertThat(getNode.getResultList().size())
                .isEqualTo(0);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);

        assertThat(getPhases.getResultList().size())
                .isEqualTo(1);

        assertThat(res)
                .isEqualTo(1);
    }
}