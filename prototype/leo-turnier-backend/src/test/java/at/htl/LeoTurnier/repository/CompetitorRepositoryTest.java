package at.htl.LeoTurnier.repository;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompetitorRepositoryTest {

    @Inject
    CompetitorRepository repository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    TeamRepository teamRepository;

    private final Team defaultTeam1 = new Team("defaultTeam1");
    private final Player defaultPlayer1 = new Player("defaultPlayer1", defaultTeam1);

    private void insertTestData() {
        repository.add(defaultPlayer1);
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
    void TestAdd01_PassNull_ShouldReturnNull() {
        // arrange

        // act
        Competitor res = repository.add(null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(1020)
    void TestAdd02_AddPlayer_ShouldAddPlayer() {
        // arrange
        String namePlayer1 = "Player1";
        Player player1 = new Player(namePlayer1);

        // act
        repository.add(player1);

        // assert
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

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
        String nameTeam1 = "Team1";
        Team team1 = new Team(nameTeam1);

        // act
        repository.add(team1);

        // assert
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

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
        String namePlayer1 = "Player1";
        String nameTeam1 = "Team1";
        Team team1 = new Team(nameTeam1);
        Player player1 = new Player(namePlayer1, team1);

        // act
        repository.add(player1);
        repository.add(team1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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
    }


    @Test
    @Order(1050)
    void TestAdd05_AddTeamThenPlayer_ShouldAddTeamAndPlayer() {
        // arrange
        String namePlayer1 = "Player1";
        String nameTeam1 = "Team1";
        Team team1 = new Team(nameTeam1);
        Player player1 = new Player(namePlayer1, team1);

        // act
        repository.add(team1);
        repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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
    }

    @Test
    @Order(1060)
    void TestAdd06_AddPlayerWithNotExistingTeam_ShouldAddPlayerAndTeam() {
        // arrange
        String namePlayer1 = "Player1";
        String nameTeam1 = "Team1";
        Player player1 = new Player(namePlayer1, new Team(nameTeam1));

        // act
        repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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
    }

    @Test
    @Order(1070)
    void TestAdd07_AddPlayerWithNotExistingTeamContainingThePlayer_ShouldAddPlayerAndTeam() {
        // arrange
        String namePlayer1 = "Player1";
        String nameTeam1 = "Team1";
        Team team1 = new Team(nameTeam1);
        Player player1 = new Player(namePlayer1, team1);

        // act
        repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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
    }

    @Test
    @Order(1080)
    void TestAdd08_AddExistingCompetitor_ShouldReturnExistingCompetitor() {
        // arrange
        String namePlayer1 = "Player1";
        Player player1 = new Player(namePlayer1);

        // act
        repository.add(player1);
        Competitor res = repository.add(player1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(1);
        assertThat(getPlayers.getResultList().get(0).getName())
                .isEqualTo(namePlayer1);

        assertThat(res.getId())
                .isEqualTo(player1.getId());
    }

    @Test
    @Order(2010)
    void TestModify01_ModifyToNull_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Competitor res = repository.modify(defaultPlayer1.getId(), null);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2020)
    void TestModify02_ModifyNotExistingCompetitor_ShouldReturnNull() {
        insertTestData();
        // arrange
        String namePlayer1 = "Player1";
        Player player1 = new Player(namePlayer1);

        // act
        Competitor res = repository.modify(-1, player1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(2030)
    void TestModify03_ModifyPlayer_ShouldModifyPlayer() {
        insertTestData();
        // arrange
        String namePlayer1 = "Player1";
        Player player1 = new Player(namePlayer1, defaultTeam1);

        // act
        repository.modify(defaultPlayer1.getId(), player1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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
    }

    @Test
    @Order(2040)
    void TestModify04_ModifyTeam_ShouldModifyTeam() {
        insertTestData();
        // arrange
        String nameTeam1 = "Team1";
        Team team1 = new Team(nameTeam1);

        // act
        repository.modify(defaultTeam1.getId(), team1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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
    }

    @Test
    @Order(2050)
    void TestModify05_ModifyToPlayerWithNotExistingTeam_ShouldAddNewTeam() {
        insertTestData();
        // arrange
        String namePlayer1 = "Player1";
        String nameTeam1 = "Team1";
        Player player1 = new Player(namePlayer1, new Team(nameTeam1));

        // act
        repository.modify(defaultPlayer1.getId(), player1);

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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

        assertThat(getTeams.getResultList().get(1).getName())
                .isEqualTo(nameTeam1);
        assertThat(getTeams.getResultList().get(1).getClass())
                .isEqualTo(Team.class);
    }

    @Test
    @Order(3010)
    void TestGetById01_SearchNotExistingCompetitor_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Competitor res = repository.getById((long) -1);

        // assert
        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(3020)
    void TestGetById02_SearchCompetitor_ShouldReturnCompetitor() {
        insertTestData();
        // arrange

        // act
        Competitor res = repository.getById(defaultPlayer1.getId());

        // assert
        assertThat(res.getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(3110)
    void TestGetByTeamId01_SearchNotExistingCompetitor_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        List<Player> res = playerRepository.getByTeamId((long) -1);

        // assert
        assertThat(res.size())
                .isEqualTo(0);
    }

    @Test
    @Order(3120)
    void TestGetByTeamId02_SearchCompetitor_ShouldReturnCompetitor() {
        insertTestData();
        // arrange

        // act
        List<Player> res = playerRepository.getByTeamId(defaultTeam1.getId());

        // assert
        assertThat(res.get(0).getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(4010)
    void TestGetAll01_SearchNone_ShouldReturnEmptyList() {
        // arrange

        // act
        List<Competitor> res = repository.getAll();

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
        List<Competitor> res = repository.getAll();

        // assert
        assertThat(res.size())
                .isEqualTo(2);
        assertThat(res.get(0).getName())
                .isEqualTo(defaultTeam1.getName());
        assertThat(res.get(1).getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(5010)
    void TestDelete01_DeleteNotExistingCompetitor_ShouldReturnNull() {
        insertTestData();
        // arrange

        // act
        Competitor res = repository.delete((long) -1);

        // assert

        assertThat(res)
                .isEqualTo(null);
    }

    @Test
    @Order(5020)
    void TestDelete02_DeletePlayer_ShouldDeletePlayer() {
        insertTestData();
        // arrange

        // act
        Competitor res = repository.delete(defaultPlayer1.getId());

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

        assertThat(getPlayers.getResultList().size())
                .isEqualTo(0);

        assertThat(getTeams.getResultList().size())
                .isEqualTo(1);
        assertThat(getTeams.getResultList().get(0).getName())
                .isEqualTo(defaultTeam1.getName());
        assertThat(getTeams.getResultList().get(0).getClass())
                .isEqualTo(Team.class);

        assertThat(res.getName())
                .isEqualTo(defaultPlayer1.getName());
    }

    @Test
    @Order(5030)
    void TestDelete03_DeleteTeam_ShouldDeleteTeam() {
        insertTestData();
        // arrange

        // act
        Competitor res = repository.delete(defaultTeam1.getId());

        // assert
        TypedQuery<Player> getPlayers = repository.getEntityManager().createQuery("select p from Player p", Player.class);
        TypedQuery<Team> getTeams = repository.getEntityManager().createQuery("select t from Team t", Team.class);

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

        assertThat(res.getName())
                .isEqualTo(defaultTeam1.getName());
    }

    @Test
    @Order(6010)
    void TestClear01_Clear_ShouldDeleteAll() {
        insertTestData();
        // arrange

        // act
        long res = repository.clear();

        // assert
        TypedQuery<Competitor> getCompetitors = repository.getEntityManager().createQuery("select c from Competitor c", Competitor.class);

        assertThat(getCompetitors.getResultList().size())
                .isEqualTo(0);
        assertThat(res)
                .isEqualTo(2);
    }
}