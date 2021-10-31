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
        defaultTeam1.setPlayers(List.of(defaultPlayer1));
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    @Order(1010)
    void TestAdd01_PassNull_ShouldThrowIllegalArgumentException() {
        // arrange
        String message = "";
        // act
        try {
            repository.add(null);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        // assert
        assertThat(message)
                .isEqualTo("Competitor is null.");
    }

    @Test
    @Order(1020)
    void TestAdd02_AddPlayer_ShouldAddPlayer() {
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
    @Order(1030)
    void TestAdd03_AddTeam_ShouldAddTeam() {
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
    @Order(1040)
    void TestAdd04_AddPlayerThenTeam_ShouldAddPlayerAndTeam() {
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
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


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
    @Order(1050)
    void TestAdd05_AddTeamThenPlayer_ShouldAddTeamAndPlayer() {
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
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


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
    @Order(1060)
    void TestAdd06_AddPlayerWithNotExistingTeam_ShouldAddPlayerAndTeam() {
        // arrange
        String nameTeam1 = "T1";
        String namePlayer1 = "Faker";
        Player player1 = new Player(namePlayer1, new Team(nameTeam1));

        // act
        repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


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
    @Order(1070)
    void TestAdd07_AddTeamWithNotExistingPlayer_ShouldAddTeamAndPlayer() {
        // arrange
        String nameTeam1 = "T1";
        String namePlayer1 = "Faker";
        Team team1 = new Team(nameTeam1,
                List.of(new Player(namePlayer1)));

        // act
        repository.add(team1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


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
    @Order(1080)
    void TestAdd08_AddExistingCompetitor_ShouldThrowIllegalArgumentException() {
        // arrange
        String message = "";

        String namePlayer1 = "Faker";
        Player player1 = new Player(namePlayer1);

        // act
        repository.add(player1);
        try {
            repository.add(player1);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }

        // assert
        assertThat(message)
                .isEqualTo("Competitor already exists.");
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldThrowIllegalArgumentException() {
        insertTestData();
        // arrange
        String message = "";

        // act
        try {
            repository.modify(defaultPlayer1.getId(), null);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }

        // assert
        assertThat(message)
                .isEqualTo("Competitor is null.");
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingCompetitor_ShouldThrowIllegalArgumentException() {
        insertTestData();
        // arrange
        String message = "";

        String namePlayer1 = "Faker";
        Player player1 = new Player(namePlayer1);

        // act
        try {
            repository.modify(-1, player1);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }

        // assert
        assertThat(message)
                .isEqualTo("Competitor with Id -1 does not exist.");
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyPlayer_ShouldModifyPlayer() {
        insertTestData();
        // arrange

        String namePlayer1 = "Faker";
        Player player1 = new Player(namePlayer1, defaultTeam1);

        // act

        repository.modify(defaultPlayer1.getId(), player1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        List<Competitor> competitors = entityManager.createQuery("select c from Competitor c", Competitor.class).getResultList();

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(defaultTeam1.getName());


        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(defaultTeam1.getName());
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getName())
                .isEqualTo(namePlayer1);
    }

    @Test
    @Order(2040)
    void TestModify04_ModifyTeam_ShouldModifyTeam() {
        insertTestData();
        // arrange

        String nameTeam1 = "T1";
        Team team1 = new Team(nameTeam1, defaultTeam1.getPlayers());

        // act

        repository.modify(defaultTeam1.getId(), team1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(defaultPlayer1.getName());
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(2050)
    void TestModify05_ModifyToPlayerWithNotExistingTeam_ShouldAddNewTeam() {
        insertTestData();
        // arrange
        String namePlayer1 = "Faker";
        String nameTeam1 = "T1";
        Player player1 = new Player(namePlayer1, new Team(nameTeam1));

        // act

        repository.modify(defaultPlayer1.getId(), player1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);


        assertThat(getTeams.getResultList().size())
                .isEqualTo(2);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(defaultTeam1.getName());
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().size())
                .isEqualTo(0);

        assertThat(getTeams.getResultList().get(1).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(1).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(1).getPlayers().get(0).getName())
                .isEqualTo(namePlayer1);
    }

    @Test
    @Order(2060)
    void TestModify06_ModifyToTeamWithNotExistingPlayer_ShouldAddNewPlayer() {
        insertTestData();
        // arrange
        String namePlayer1 = "Faker";
        String nameTeam1 = "T1";
        Team team1 = new Team(nameTeam1,
                List.of(new Player(namePlayer1)));

        // act

        repository.modify(defaultTeam1.getId(), team1);

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(2);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(defaultPlayer1.getName());
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam())
                .isEqualTo(null);

        assertThat(getPlayers.getResultList().get(1).getName())
                .isEqualTo(namePlayer1);
        assertThat(getPlayers.getResultList().get(1).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(1).getTeam().getName())
                .isEqualTo(nameTeam1);


        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().get(0).getTeam().getName())
                .isEqualTo(nameTeam1);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingCompetitor_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Competitor competitor = repository.getById((long) -1);

        // assert
        assertThat(competitor)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchCompetitor_ShouldReturnCompetitor() {
        insertTestData();
        // arrange

        // act
        Competitor competitor = repository.getById(defaultPlayer1.getId());

        // assert
        assertThat(competitor.getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<Competitor> competitors = repository.getAll();

        // assert
        assertThat(competitors.size())
                .isEqualTo(0);
    }

    @Test
    @Order(4020)
    void TestGetAll02_SearchAll_ShouldReturnAll() {
        insertTestData();
        // arrange

        // act
        List<Competitor> competitors = repository.getAll();

        // assert
        assertThat(competitors.size())
                .isEqualTo(2);
        assertThat(competitors.get(0).getName())
                .isEqualTo(defaultTeam1.getName());
        assertThat(competitors.get(1).getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingCompetitor_ShouldThrowIllegalArgumentException() {
        insertTestData();
        // arrange
        String message = "";

        // act
        try {
            repository.delete((long) -1);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }

        // assert

        assertThat(message)
                .isEqualTo("Competitor with Id -1 does not exist.");
    }

    @Test
    @Order(5020)
    void TestDelete02_DeletePlayer_ShouldDeletePlayer() {
        insertTestData();
        // arrange

        // act
        Competitor player = repository.delete(defaultPlayer1.getId());

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(0);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(defaultTeam1.getName());
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);
        assertThat(getTeams.getResultList().get(0).getPlayers().size())
                .isEqualTo(0);

        assertThat(player.getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(5030)
    void TestDelete03_DeleteTeam_ShouldDeleteTeam() {
        insertTestData();
        // arrange

        // act
        Competitor team = repository.delete(defaultTeam1.getId());

        // assert
        TypedQuery<Player> getPlayers = entityManager.createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = entityManager.createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(defaultPlayer1.getName());
        assertThat(getPlayers.getResultList().get(0).getClass())
                .isEqualTo(Player.class);
        assertThat(getPlayers.getResultList().get(0).getTeam())
                .isEqualTo(null);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(0);

        assertThat(team.getName())
                .isEqualTo(defaultTeam1.getName());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long deleted = repository.clear();

        // assert
        TypedQuery<Competitor> getCompetitors = entityManager.createQuery("select c from Competitor c", Competitor.class);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(0);
        assertThat(deleted)
                .isEqualTo(2);
    }
}