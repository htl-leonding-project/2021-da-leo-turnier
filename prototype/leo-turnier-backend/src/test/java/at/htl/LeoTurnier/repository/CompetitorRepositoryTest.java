package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.*;
import org.postgresql.core.NativeQuery;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompetitorRepositoryTest {

    @Inject
    CompetitorRepository repository;

    @Inject
    EntityManager entityManager;

    private final Team defaultTeam1 = new Team("DA");
    private final Player defaultPlayer1 = new Player("Venia", defaultTeam1);

    private void insertTestData() {
        repository.add(defaultPlayer1);
        repository.add(defaultTeam1);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    @Order(101)
    void Test01_add_AddNull_ShouldNotAdd() {
        // arrange

        // act
        repository.add(null);

        // assert
        Query getAll = entityManager.createNativeQuery("select c from C_COMPETITOR c", Competitor.class);
        assertThat(getAll.getResultList().size())
                .isEqualTo(0);
    }

    @Test
    @Order(102)
    void Test02_add_AddPlayer_ShouldAddPlayer() {
        // arrange
        String namePlayer1 = "Faker";
        Player player1 = new Player(namePlayer1);

        // act
        repository.add(player1);

        // assert
        TypedQuery<Competitor> getCompetitors = entityManager.createQuery("select c from Competitor c", Competitor.class);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(1);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getCompetitors.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
    }

    @Test
    @Order(103)
    void Test03_add_AddTeam_ShouldAddTeam() {
        // arrange
        String nameTeam1 = "T1";
        Team team1 = new Team(nameTeam1);

        // act
        repository.add(team1);

        // assert
        TypedQuery<Competitor> getCompetitors = entityManager.createQuery("select c from Competitor c", Competitor.class);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(1);
        assertThat(getCompetitors.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getCompetitors.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
    }

    @Test
    @Order(104)
    void Test04_add_AddPlayerThenTeam_ShouldAddPlayerAndTeam() {
        // arrange
        String namePlayer1 = "Faker";
        String nameTeam1 = "T1";
        Player player1 = new Player(namePlayer1);
        Team team1 = new Team(nameTeam1,
                List.of(player1));

        // act
        repository.add(player1);
        repository.add(team1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getName())
                .isEqualTo(namePlayer1);
    }


    @Test
    @Order(105)
    void Test05_add_AddTeamThenPlayer_ShouldAddTeamAndPlayer() {
        // arrange
        String nameTeam1 = "T1";
        String namePlayer1 = "Faker";
        Team team1 = new Team(nameTeam1);
        Player player1 = new Player(namePlayer1, team1);

        // act
        repository.add(team1);
        repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getName())
                .isEqualTo(namePlayer1);
    }

    @Test
    @Order(106)
    void Test06_add_AddPlayerWithTeam_ShouldAddPlayerAndTeam() {
        // arrange
        String nameTeam1 = "T1";
        String namePlayer1 = "Faker";
        Player player1 = new Player(namePlayer1, new Team(nameTeam1));

        // act
        repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getName())
                .isEqualTo(namePlayer1);
    }

    @Test
    @Order(107)
    void Test07_add_AddTeamWithPlayer_ShouldAddTeamAndPlayer() {
        // arrange
        String nameTeam1 = "T1";
        String namePlayer1 = "Faker";
        Team team1 = new Team(nameTeam1,
                List.of(new Player(namePlayer1)));

        // act
        repository.add(team1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getName())
                .isEqualTo(namePlayer1);
    }

    @Test
    @Order(201)
    void Test08_modify_AddTeamWithPlayer_ShouldAddTeamAndPlayer() {
        insertTestData();

    }
}