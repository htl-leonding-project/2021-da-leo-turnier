package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.SportType;
import at.htl.LeoTurnier.entity.Team;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.entity.TournamentMode;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TournamentModeRepositoryTest {

    @Inject
    TournamentModeRepository repository;

    @Inject
    TournamentRepository tournamentRepository;

    private final TournamentMode defaultTournamentMode1 = new TournamentMode("GROUPS");
    private final Tournament defaultTournament1 = new Tournament("MSI", defaultTournamentMode1);

    private void insertTestData() {
        repository.add(defaultTournamentMode1);
        tournamentRepository.add(defaultTournament1);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        tournamentRepository.clear();
        repository.clear();
    }

    @Test
    @Order(1010)
    void TestAdd01_PassNull_ShouldReturnNull() {
        // arrange

        // act
        TournamentMode res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddTournamentMode_ShouldAddTournamentMode() {
        // arrange
        String nameTournamentMode1 = "KO";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);

        // act
        repository.add(tournamentMode1);

        // assert
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(nameTournamentMode1);
    }

    @Test
    @Order(1030)
    void TestAdd03_AddExistingTournamentMode_ShouldReturnExistingTournamentMode() {
        // arrange
        String nameTournamentMode1 = "KO";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);

        // act
        repository.add(tournamentMode1);
        TournamentMode res = repository.add(tournamentMode1);

        // assert
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(nameTournamentMode1);

        assertThat(res.getId())
                .isEqualTo(tournamentMode1.getId());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        TournamentMode res = repository.modify(defaultTournamentMode1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingTournamentMode_ShouldReturnNull() {
        insertTestData();
        // arrange
        String nameTournamentMode1 = "KO";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);

        // act
        TournamentMode res = repository.modify(-1, tournamentMode1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyTournamentMode_ShouldModifyTournamentMode() {
        insertTestData();
        // arrange
        String nameTournamentMode1 = "KO";
        TournamentMode tournamentMode1 = new TournamentMode(nameTournamentMode1);

        // act
        repository.modify(defaultTournamentMode1.getId(), tournamentMode1);

        // assert
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournamentModes.getResultList().get(0).getName())
                .isEqualTo(nameTournamentMode1);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingTournamentMode_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        TournamentMode res = repository.getById((long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchTournamentMode_ShouldReturnTournamentMode() {
        insertTestData();
        // arrange

        // act
        TournamentMode res = repository.getById(defaultTournamentMode1.getId());

        // assert
        assertThat(res.getName())
                .isEqualTo(defaultTournamentMode1.getName());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<TournamentMode> res = repository.getAll();

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
        List<TournamentMode> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(1);
        assertThat(res.get(0).getName())
                .isEqualTo(defaultTournamentMode1.getName());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingTournamentMode_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        TournamentMode res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeleteTournamentMode_ShouldDeleteTournamentMode() {
        insertTestData();
        // arrange

        // act
        TournamentMode res = repository.delete(defaultTournamentMode1.getId());

        // assert
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(0);

        assertThat(res.getName())
                .isEqualTo(defaultTournamentMode1.getName());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<TournamentMode> getTournamentModes = repository.getEntityManager().createQuery("select tm from TournamentMode tm", TournamentMode.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournamentModes.getResultList().size())
                .isEqualTo(0);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);

        assertThat(res)
                .isEqualTo(1);
    }
}