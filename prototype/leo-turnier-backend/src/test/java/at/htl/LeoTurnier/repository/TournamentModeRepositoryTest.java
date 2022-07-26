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

    private final String defaultTournamentMode1 = "Elimination";
    private final String defaultTournamentMode2 = "Round Robin";
    private final String defaultTournamentMode3 = "Combination";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1010)
    void TestGetById02_SearchTournamentMode_ShouldReturnTournamentMode() {
        // arrange

        // act
        TournamentMode elimination = repository.getById(1L);
        TournamentMode roundRobin = repository.getById(2L);
        TournamentMode combination = repository.getById(3L);

        // assert
        assertThat(elimination.getName())
                .isEqualTo(defaultTournamentMode1);
        assertThat(roundRobin.getName())
                .isEqualTo(defaultTournamentMode2);
        assertThat(combination.getName())
                .isEqualTo(defaultTournamentMode3);
    }

    @Test
    @Order(2010)
    void TestGetAll02_SearchAll_ShouldReturnAll() {
        // arrange

        // act
        List<TournamentMode> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(3);
        assertThat(res.get(0).getName())
                .isEqualTo(defaultTournamentMode1);
        assertThat(res.get(1).getName())
                .isEqualTo(defaultTournamentMode2);
        assertThat(res.get(2).getName())
                .isEqualTo(defaultTournamentMode3);
    }
}