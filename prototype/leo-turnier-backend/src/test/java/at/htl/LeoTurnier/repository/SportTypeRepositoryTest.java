package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.SportType;
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
class SportTypeRepositoryTest {

    @Inject
    SportTypeRepository repository;

    @Inject
    TournamentRepository tournamentRepository;

    private final SportType defaultSportType1 = new SportType("defaultSportType1");
    private final Tournament defaultTournament1 = new Tournament("defaultTournament1", defaultSportType1);

    private void insertTestData() {
        repository.add(defaultSportType1);
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
        SportType res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddSportType_ShouldAddSportType() {
        // arrange
        String nameSportType1 = "SportType1";
        SportType sportType1 = new SportType(nameSportType1);

        // act
        repository.add(sportType1);

        // assert
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(nameSportType1);
    }

    @Test
    @Order(1030)
    void TestAdd03_AddExistingSportType_ShouldReturnExistingSportType() {
        // arrange
        String nameSportType1 = "SportType1";
        SportType sportType1 = new SportType(nameSportType1);

        // act
        repository.add(sportType1);
        SportType res = repository.add(sportType1);

        // assert
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(nameSportType1);

        assertThat(res.getId())
                .isEqualTo(sportType1.getId());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        SportType res = repository.modify(defaultSportType1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingSportType_ShouldReturnNull() {
        insertTestData();
        // arrange

        String nameSportType1 = "SportType1";
        SportType sportType1 = new SportType(nameSportType1);

        // act
        SportType res = repository.modify(-1, sportType1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifySportType_ShouldModifySportType() {
        insertTestData();
        // arrange


        String nameSportType1 = "SportType1";
        SportType sportType1 = new SportType(nameSportType1);

        // act
        repository.modify(defaultSportType1.getId(), sportType1);

        // assert
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(1);
        assertThat(getSportTypes.getResultList().get(0).getName())
                .isEqualTo(nameSportType1);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingSportType_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        SportType res = repository.getById((long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchSportType_ShouldReturnSportType() {
        insertTestData();
        // arrange

        // act
        SportType res = repository.getById(defaultSportType1.getId());

        // assert
        assertThat(res.getName())
                .isEqualTo(defaultSportType1.getName());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<SportType> res = repository.getAll();

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
        List<SportType> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(1);
        assertThat(res.get(0).getName())
                .isEqualTo(defaultSportType1.getName());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingSportType_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        SportType res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeleteSportType_ShouldDeleteSportType() {
        insertTestData();
        // arrange

        // act
        SportType res = repository.delete(defaultSportType1.getId());

        // assert
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);
        assertThat(getTournaments.getResultList().get(0).getName())
                .isEqualTo(defaultTournament1.getName());

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(0);

        assertThat(res.getName())
                .isEqualTo(defaultSportType1.getName());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<SportType> getSportTypes = repository.getEntityManager().createQuery("select st from SportType st", SportType.class);
        TypedQuery<Tournament> getTournaments = repository.getEntityManager().createQuery("select t from Tournament t", Tournament.class);

        assertThat(getSportTypes.getResultList().size())
                .isEqualTo(0);

        assertThat(getTournaments.getResultList().size())
                .isEqualTo(1);

        assertThat(res)
                .isEqualTo(1);
    }
}