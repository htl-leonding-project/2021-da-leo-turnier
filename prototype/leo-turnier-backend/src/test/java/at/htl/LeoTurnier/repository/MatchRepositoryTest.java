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
class MatchRepositoryTest {

    @Inject
    MatchRepository repository;

    @Inject
    NodeRepository nodeRepository;

    @Inject
    CompetitorRepository competitorRepository;

    private final Team defaultCompetitor1 = new Team("DK");
    private final Team defaultCompetitor2 = new Team("FNC");
    private final Match defaultMatch1 = new Match(defaultCompetitor1, defaultCompetitor2);
    private final Node defaultNode1 = new Node(defaultMatch1);

    private void insertTestData() {
        repository.add(defaultMatch1);
        nodeRepository.add(defaultNode1);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.clear();
        competitorRepository.clear();
        nodeRepository.clear();
    }

    @Test
    @Order(1010)
    void TestAdd01_PassNull_ShouldReturnNull() {
        // arrange

        // act
        Match res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddMatch_ShouldAddMatch() {
        // arrange
        Match match1 = new Match();

        // act
        repository.add(match1);

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(1030)
    void TestAdd03_AddMatchWithExistingCompetitors_ShouldAddMatch() {
        // arrange
        Match match1 = new Match(defaultCompetitor1, defaultCompetitor2);

        // act
        competitorRepository.add(defaultCompetitor1);
        competitorRepository.add(defaultCompetitor2);
        repository.add(match1);

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
        assertThat(getMatches.getResultList().get(0).getCompetitor1().getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getMatches.getResultList().get(0).getCompetitor2().getName())
                .isEqualTo(defaultCompetitor2.getName());

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(2);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getCompetitors.getResultList().get(1).getName())
                .isEqualTo(defaultCompetitor2.getName());
    }

    @Test
    @Order(1040)
    void TestAdd04_AddMatchWithNotExistingCompetitors_ShouldAddMatch() {
        // arrange
        Match match1 = new Match(defaultCompetitor1, defaultCompetitor2);

        // act
        repository.add(match1);

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
        assertThat(getMatches.getResultList().get(0).getCompetitor1().getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getMatches.getResultList().get(0).getCompetitor2().getName())
                .isEqualTo(defaultCompetitor2.getName());

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(2);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getCompetitors.getResultList().get(1).getName())
                .isEqualTo(defaultCompetitor2.getName());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Match res = repository.modify(defaultMatch1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingMatch_ShouldReturnNull() {
        insertTestData();
        // arrange
        Match match1 = new Match();

        // act
        Match res = repository.modify(-1, match1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyMatch_ShouldModifyMatch() {
        insertTestData();
        // arrange
        Match match1 = new Match();

        // act
        repository.modify(defaultMatch1.getId(), match1);

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
    }

    @Test
    @Order(2040)
    void TestModify04_ModifyMatchWithExistingCompetitors_ShouldModifyMatch() {
        insertTestData();
        // arrange
        Competitor competitor1 = new Team("G2");

        Match match1 = new Match(defaultCompetitor1, defaultCompetitor2);

        // act
        repository.modify(defaultMatch1.getId(), match1);

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
        assertThat(getMatches.getResultList().get(0).getCompetitor1().getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getMatches.getResultList().get(0).getCompetitor2().getName())
                .isEqualTo(defaultCompetitor2.getName());

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(2);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getCompetitors.getResultList().get(1).getName())
                .isEqualTo(defaultCompetitor2.getName());
    }

    @Test
    @Order(2050)
    void TestModify05_ModifyMatchWithNotExistingCompetitors_ShouldModifyMatch() {
        insertTestData();
        // arrange
        String nameCompetitor1 = "G2";
        String nameCompetitor2 = "FPX";
        Competitor competitor1 = new Team(nameCompetitor1);
        Competitor competitor2 = new Team(nameCompetitor2);
        Match match1 = new Match(competitor1, competitor2);

        // act
        repository.modify(defaultMatch1.getId(), match1);

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

        assertThat(getMatches.getResultList().size())
                .isEqualTo(1);
        assertThat(getMatches.getResultList().get(0).getCompetitor1().getName())
                .isEqualTo(nameCompetitor1);
        assertThat(getMatches.getResultList().get(0).getCompetitor2().getName())
                .isEqualTo(nameCompetitor2);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(4);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getCompetitors.getResultList().get(1).getName())
                .isEqualTo(defaultCompetitor2.getName());
        assertThat(getCompetitors.getResultList().get(2).getName())
                .isEqualTo(nameCompetitor1);
        assertThat(getCompetitors.getResultList().get(3).getName())
                .isEqualTo(nameCompetitor2);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingMatch_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Match res = repository.getById((long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchMatch_ShouldReturnMatch() {
        insertTestData();
        // arrange

        // act
        Match res = repository.getById(defaultMatch1.getId());

        // assert
        assertThat(res.getCompetitor1().getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(res.getCompetitor2().getName())
                .isEqualTo(defaultCompetitor2.getName());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<Match> res = repository.getAll();

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
        List<Match> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(1);
        assertThat(res.get(0).getCompetitor1().getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(res.get(0).getCompetitor2().getName())
                .isEqualTo(defaultCompetitor2.getName());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingMatch_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Match res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeleteMatch_ShouldDeleteMatch() {
        insertTestData();
        // arrange

        // act
        Match res = repository.delete(defaultMatch1.getId());

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);


        assertThat(getMatches.getResultList().size())
                .isEqualTo(0);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(2);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getCompetitors.getResultList().get(1).getName())
                .isEqualTo(defaultCompetitor2.getName());

        assertThat(getNodes.getResultList().size())
                .isEqualTo(0);

        assertThat(res.getCompetitor1().getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(res.getCompetitor2().getName())
                .isEqualTo(defaultCompetitor2.getName());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<Match> getMatches = repository.getEntityManager().createQuery("select m from Match m", Match.class);
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);
        TypedQuery<Node> getNodes = repository.getEntityManager().createQuery("select n from Node n", Node.class);


        assertThat(getMatches.getResultList().size())
                .isEqualTo(0);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(2);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(defaultCompetitor1.getName());
        assertThat(getCompetitors.getResultList().get(1).getName())
                .isEqualTo(defaultCompetitor2.getName());

        assertThat(getNodes.getResultList().size())
                .isEqualTo(0);

        assertThat(res)
                .isEqualTo(1);
    }
}