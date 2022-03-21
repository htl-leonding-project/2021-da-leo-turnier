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
class TournamentRepositoryTest {

    @Inject
    TournamentRepository repository;

    @Inject
    SportTypeRepository sportTypeRepository;

    @Inject
    TournamentModeRepository tournamentModeRepository;

    @Inject
    PhaseRepository phaseRepository;

    @Inject
    MatchRepository matchRepository;

    @Inject
    NodeRepository nodeRepository;

    private final SportType defaultSportType1 = new SportType("defaultSportType1");
    private final TournamentMode defaultTournamentMode1 = new TournamentMode("defaultTournamentMode1");
    private final Tournament defaultTournament1 = new Tournament("defaultTournament1", defaultSportType1, defaultTournamentMode1);
    private final Phase defaultPhase1 = new Phase(1, defaultTournament1);
    private final Match defaultMatch1 = new Match();
    private final Node defaultNode1 = new Node(defaultMatch1, defaultPhase1);

    private void insertTestData() {
        repository.add(defaultTournament1);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        tournamentModeRepository.clear();
        sportTypeRepository.clear();
        repository.clear();
    }

    @Test
    @Order(1010)
    void TestAdd01_PassNull_ShouldReturnNull() {
        // arrange

        // act
        Tournament res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddTournament_ShouldAddTournament() {
        // arrange
        String nameTournament1 = "Tournament1";
        Tournament tournament1 = new Tournament(nameTournament1);

        // act
        repository.add(tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);
    }

    @Test
    @Order(1030)
    void TestAdd03_AddTournamentWithExistingSportType_ShouldAddTournament() {
        // arrange
        String nameSportType1 = "SportType1";
        String nameTournament1 = "Tournament1";
        SportType sportType1 = new SportType(nameSportType1);
        Tournament tournament1 = new Tournament(nameTournament1, sportType1);

        // act
        sportTypeRepository.add(sportType1);
        repository.add(tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(nameSportType1);
        assertThat(getSportTypes.getResultList().get(0).getClass())
                .isEqualTo(SportType.class);
    }

    @Test
    @Order(1040)
    void TestAdd04_AddTournamentWithNotExistingSportType_ShouldAddTournament() {
        // arrange
        String nameSportType1 = "SportType1";
        String nameTournament1 = "Tournament1";
        SportType sportType1 = new SportType(nameSportType1);
        Tournament tournament1 = new Tournament(nameTournament1, sportType1);

        // act
        repository.add(tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(nameSportType1);
        assertThat(getSportTypes.getResultList().get(0).getClass())
                .isEqualTo(SportType.class);
    }

    @Test
    @Order(1050)
    void TestAdd05_AddTournamentWithExistingTournamentMode_ShouldAddTournament() {
        // arrange
        String nameTournamentMode1 = "TournamentMode1";
        String nameTournament1 = "Tournament1";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);
        Tournament tournament1 = new Tournament(nameTournament1, tournamentMode1);

        // act
        tournamentModeRepository.add(tournamentMode1);
        repository.add(tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(nameTournamentMode1);
        assertThat(getTournamentModes.getResultList().get(0).getClass())
                .isEqualTo(TournamentMode.class);
    }

    @Test
    @Order(1060)
    void TestAdd06_AddTournamentWithNotExistingTournamentMode_ShouldAddTournament() {
        // arrange
        String nameTournamentMode1 = "TournamentMode1";
        String nameTournament1 = "Tournament1";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);
        Tournament tournament1 = new Tournament(nameTournament1, tournamentMode1);

        // act
        repository.add(tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(nameTournamentMode1);
        assertThat(getTournamentModes.getResultList().get(0).getClass())
                .isEqualTo(TournamentMode.class);
    }

    @Test
    @Order(1070)
    void TestAdd07_AddExistingTournament_ShouldReturnExistingTournament() {
        // arrange
        String nameTournament1 = "Tournament1";
        Tournament tournament1 = new Tournament(nameTournament1);

        // act
        repository.add(tournament1);
        Tournament res = repository.add(tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(res.getId())
                .isEqualTo(tournament1.getId());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Tournament res = repository.modify(defaultTournament1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingTournament_ShouldReturnNull() {
        insertTestData();
        // arrange
        String nameTournament1 = "Tournament1";
        Tournament tournament1 = new Tournament(nameTournament1);

        // act
        Tournament res = repository.modify(-1, tournament1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyTournament_ShouldModifyTournament() {
        insertTestData();
        // arrange
        String nameTournament1 = "Tournament1";
        Tournament tournament1 = new Tournament(nameTournament1);

        // act
        repository.modify(defaultTournament1.getId(), tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);
    }

    @Test
    @Order(2040)
    void TestModify04_ModifyTournamentWithExistingSportType_ShouldAddTournament() {
        insertTestData();
        // arrange
        String nameTournament1 = "Tournament1";
        Tournament tournament1 = new Tournament(nameTournament1, defaultSportType1);

        // act
        sportTypeRepository.add(defaultSportType1);
        repository.modify(defaultTournament1.getId(), tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(defaultSportType1.getName());
    }

    @Test
    @Order(2050)
    void TestModify05_ModifyTournamentWithNotExistingSportType_ShouldAddTournament() {
        insertTestData();
        // arrange
        String nameSportType1 = "SportType1";
        String nameTournament1 = "Tournament1";
        SportType sportType1 = new SportType(nameSportType1);
        Tournament tournament1 = new Tournament(nameTournament1, sportType1);

        // act
        repository.modify(defaultTournament1.getId(), tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(2);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(defaultSportType1.getName());

        assertThat(getSportTypes.getResultList().get(1).getName())
                .isEqualTo(nameSportType1);
    }

    @Test
    @Order(2060)
    void TestModify06_ModifyTournamentWithExistingTournamentMode_ShouldAddTournament() {
        insertTestData();
        // arrange
        String nameTournament1 = "Tournament1";
        Tournament tournament1 = new Tournament(nameTournament1, defaultTournamentMode1);

        // act
        tournamentModeRepository.add(defaultTournamentMode1);
        repository.modify(defaultTournament1.getId(), tournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(nameTournament1);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(defaultTournamentMode1.getName());
    }

    @Test
    @Order(2060)
    void TestModify06_ModifyTournamentWithNotExistingTournamentMode_ShouldAddTournament() {
        insertTestData();
        // arrange
        String nameTournamentMode1 = "TournamentMode1";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);
        defaultTournament1.setTournamentMode(tournamentMode1);

        // act
        repository.modify(defaultTournament1.getId(), defaultTournament1);

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());
        assertThat(getTournaments.getResultList().get(0).getTournamentMode().getName())
                .isEqualTo(nameTournamentMode1);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(2);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(defaultTournamentMode1.getName());

        assertThat(getTournamentModes.getResultList().get(1).getName())
                .isEqualTo(nameTournamentMode1);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingTournament_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Tournament res = repository.getById((long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchTournament_ShouldReturnTournament() {
        insertTestData();
        // arrange

        // act
        Tournament res = repository.getById(defaultTournament1.getId());

        // assert
        assertThat(res.getName())
                .isEqualTo(defaultTournament1.getName());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<Tournament> res = repository.getAll();

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
        List<Tournament> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(1);
        assertThat(res.get(0).getName())
                .isEqualTo(defaultTournament1.getName());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingTournament_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Tournament res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeleteTournament_ShouldDeleteTournament() {
        insertTestData();
        // arrange

        // act
        Tournament res = repository.delete(defaultTournament1.getId());

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(0);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(defaultSportType1.getName());

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(defaultTournamentMode1.getName());

        assertThat(res.getName())
                .isEqualTo(defaultTournament1.getName());
    }

    @Test
    @Order(5030)
    void TestDelete03_DeleteTournamentWithAllRelationsPresent_ShouldDeleteTournament() {
        insertTestData();
        phaseRepository.add(defaultPhase1);
        matchRepository.add(defaultMatch1);
        nodeRepository.add(defaultNode1);
        // arrange

        // act
        Tournament res = repository.delete(defaultTournament1.getId());

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(0);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(defaultSportType1.getName());

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(defaultTournamentMode1.getName());

        assertThat(res.getName())
                .isEqualTo(defaultTournament1.getName());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(0);
        assertThat(res)
                .isEqualTo(1);
    }
}