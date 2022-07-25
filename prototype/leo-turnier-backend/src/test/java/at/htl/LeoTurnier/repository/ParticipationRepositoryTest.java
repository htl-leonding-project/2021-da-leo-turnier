package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ParticipationRepositoryTest {

    @Inject
    ParticipationRepository repository;

    @Inject
    CompetitorRepository competitorRepository;

    @Inject
    TournamentRepository tournamentRepository;

    private final Tournament defaultTournament1 = new Tournament("defaultTournament1");
    private final Tournament defaultTournament2 = new Tournament("defaultTournament2");
    private final Player defaultPlayer1 = new Player("defaultPlayer1");
    private final Player defaultPlayer2 = new Player("defaultPlayer2");

    private void insertTestData() {
        tournamentRepository.add(defaultTournament1);
        tournamentRepository.add(defaultTournament2);
        competitorRepository.add(defaultPlayer1);
        competitorRepository.add(defaultPlayer2);
        repository.add(defaultTournament1.getId(), defaultPlayer1.getId());
        repository.add(defaultTournament1.getId(), defaultPlayer2.getId());
        repository.add(defaultTournament2.getId(), defaultPlayer1.getId());
        repository.add(defaultTournament2.getId(), defaultPlayer2.getId());
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.clear();
        competitorRepository.clear();
        tournamentRepository.clear();
    }

    @Test
    @Order(1010)
    void TestAddCompetitorToTournament01_PassNull_ShouldReturnNull() {
        // arrange
        competitorRepository.add(defaultPlayer1);
        tournamentRepository.add(defaultTournament1);

        // act
        Participation res = repository.add(null, null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAddCompetitorToTournament02_PassNotExistingTournament_ShouldReturnNull() {
        // arrange
        competitorRepository.add(defaultPlayer1);
        tournamentRepository.add(defaultTournament1);

        // act
        Participation res = repository.add((long) -1, defaultPlayer1.getId());

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1030)
    void TestAddCompetitorToTournament03_PassNotExistingCompetitor_ShouldReturnNull() {
        // arrange
        competitorRepository.add(defaultPlayer1);
        tournamentRepository.add(defaultTournament1);

        // act
        Participation res = repository.add(defaultTournament1.getId(), (long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1040)
    void TestAddCompetitorToTournament04_AddParticipation_ShouldAddParticipation() {
        // arrange
        competitorRepository.add(defaultPlayer1);
        tournamentRepository.add(defaultTournament1);

        // act
        Participation res = repository.add(defaultTournament1.getId(), defaultPlayer1.getId());

        // assert
        TypedQuery<Participation> getParticipations = repository.getEntityManager().createQuery("select pt from Participation pt", Participation.class);

        assertThat(getParticipations.getResultList().size())
                .isEqualTo(1);
        assertThat(getParticipations.getResultList().get(0).getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
        assertThat(getParticipations.getResultList().get(0).getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
    }

    @Test
    @Order(1050)
    void TestAddCompetitorToTournament05_AddExistingParticipation_ShouldAddParticipation() {
        // arrange
        competitorRepository.add(defaultPlayer1);
        tournamentRepository.add(defaultTournament1);

        // act
        repository.add(defaultTournament1.getId(), defaultPlayer1.getId());
        Participation res = repository.add(defaultTournament1.getId(), defaultPlayer1.getId());

        // assert
        TypedQuery<Participation> getParticipations = repository.getEntityManager().createQuery("select pt from Participation pt", Participation.class);

        assertThat(getParticipations.getResultList().size())
                .isEqualTo(1);
        assertThat(getParticipations.getResultList().get(0).getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
        assertThat(getParticipations.getResultList().get(0).getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());

        assertThat(res.getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
        assertThat(res.getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyNotExistingParticipation_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Participation res = repository.modifyPlacement(null, null, 1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyParticipation_ShouldModifyParticipation() {
        insertTestData();
        // arrange
        int numberPlacement1 = 1;

        // act
        Participation res = repository.modifyPlacement(defaultTournament1.getId(), defaultPlayer1.getId(), numberPlacement1);

        // assert
        TypedQuery<Participation> getParticipations = repository.getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.tournament.id = :tournamentId " +
                        "and pt.competitor.id = :competitorId", Participation.class);
        getParticipations.setParameter("tournamentId", defaultTournament1.getId());
        getParticipations.setParameter("competitorId", defaultPlayer1.getId());

        assertThat(getParticipations.getResultList().size())
                .isEqualTo(1);
        assertThat(getParticipations.getResultList().get(0).getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
        assertThat(getParticipations.getResultList().get(0).getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
        assertThat(getParticipations.getResultList().get(0).getPlacement())
                .isEqualTo(numberPlacement1);

        assertThat(res.getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
        assertThat(res.getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
        assertThat(res.getPlacement())
                .isEqualTo(numberPlacement1);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingParticipation_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Participation res = repository.getById((long) -1, (long)-1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchParticipation_ShouldReturnParticipation() {
        insertTestData();
        // arrange

        // act
        Participation res = repository.getById(defaultTournament1.getId(), defaultPlayer1.getId());

        // assert
        assertThat(res.getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
        assertThat(res.getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
    }

    @Test
    @Order(4010)
    void TestGetCompetitorsByTournament01_SearchNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        List<Participation> res = repository.getByTournamentId((long) -1);

        // assert
        assertThat(res.size())
                .isEqualTo(0);
    }

    @Test
    @Order(4020)
    void TestGetCompetitorsByTournament02_SearchByTournament_ShouldReturnParticipations() {
        insertTestData();
        // arrange

        // act
        List<Participation> res = repository.getByTournamentId(defaultTournament1.getId());

        // assert
        assertThat(res.size())
                .isEqualTo(2);

        assertThat(res.get(0).getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());

        assertThat(res.get(1).getCompetitor().getId())
                .isEqualTo(defaultPlayer2.getId());
    }

    @Test
    @Order(5010)
    void TestGetTournamentsByCompetitor01_SearchNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        List<Participation> res = repository.getByTournamentId((long) -1);

        // assert
        assertThat(res.size())
                .isEqualTo(0);
    }

    @Test
    @Order(5020)
    void TestGetTournamentsByCompetitor02_SearchByCompetitor_ShouldReturnParticipations() {
        insertTestData();
        // arrange

        // act
        List<Participation> res = repository.getByCompetitorId(defaultPlayer1.getId());

        // assert
        assertThat(res.size())
                .isEqualTo(2);

        assertThat(res.get(0).getTournament().getId())
                .isEqualTo(defaultTournament1.getId());

        assertThat(res.get(1).getTournament().getId())
                .isEqualTo(defaultTournament2.getId());
    }

    @Test
    @Order(3010)
    void TestGetAll01_SearchNone_ShouldReturnNull() {
        // arrange

        // act
        List<Participation> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(0);
    }

    @Test
    @Order(3020)
    void TestGetAll02_SearchAll_ShouldReturnAll() {
        insertTestData();
        // arrange

        // act
        List<Participation> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(4);
        assertThat(res.get(0).getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
        assertThat(res.get(0).getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
    }

    @Test
    @Order(6010)
    void TestDelete01_DeleteNotExistingParticipation_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Participation res = repository.delete((long) -1, (long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(6020)
    void TestDelete02_DeleteParticipation_ShouldDeleteParticipation() {
        insertTestData();
        // arrange

        // act
        Participation res = repository.delete(defaultTournament1.getId(), defaultPlayer1.getId());

        // assert
        TypedQuery<Participation> getParticipations = repository.getEntityManager().createQuery(
                "select pt " +
                        "from Participation pt " +
                        "where pt.tournament.id = :tournamentId " +
                        "and pt.competitor.id = :competitorId", Participation.class);
        getParticipations.setParameter("tournamentId", defaultTournament1.getId());
        getParticipations.setParameter("competitorId", defaultPlayer1.getId());

        assertThat(getParticipations.getResultList().size())
                .isEqualTo(0);

        assertThat(res.getCompetitor().getId())
                .isEqualTo(defaultPlayer1.getId());
        assertThat(res.getTournament().getId())
                .isEqualTo(defaultTournament1.getId());
    }

    @Test
    @Order(7010)
    void TestClear01_ClearParticipations_ShouldDeleteAllParticipation() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<Participation> getParticipations = repository.getEntityManager().createQuery("select pt from Participation pt ", Participation.class);

        assertThat(getParticipations.getResultList().size())
                .isEqualTo(0);

        assertThat(res)
                .isEqualTo(4);
    }
}