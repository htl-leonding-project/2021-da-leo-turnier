package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import at.htl.LeoTurnier.repository.PlayerRepository;
import at.htl.LeoTurnier.repository.TeamRepository;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/team")
public class TeamService {

    @Inject
    TeamRepository repository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Team team, @Context UriInfo info) {
        team = repository.add(team);
        if (team == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .path(Long.toString(team.getId())).build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@PathParam("id") long id, Team team, @Context UriInfo info) {
        team = repository.modify(id, team);
        if (team == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .path(Long.toString(team.getId())).build()).build();
    }

    private JsonObject buildTeamJsonObject(Team team) {
        JsonObjectBuilder teamBuilder = Json.createObjectBuilder();
        teamBuilder.add("id", team.getId());
        teamBuilder.add("name", team.getName());
        teamBuilder.add("seed", team.getSeed());
        JsonArrayBuilder playerArrayBuilder = Json.createArrayBuilder();
        team.getPlayers().forEach(p -> {
            JsonObjectBuilder playerBuilder = Json.createObjectBuilder();
            playerBuilder.add("id", p.getId());
            playerBuilder.add("name", p.getName());
            playerBuilder.add("seed", p.getSeed());
            playerBuilder.add("birthdate", p.getBirthdate().toString());
            playerArrayBuilder.add(playerBuilder);
        });
        teamBuilder.add("players", playerArrayBuilder);
        return teamBuilder.build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        return Response.ok(buildTeamJsonObject(repository.getById(id))).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Team> teams = repository.getAll();
        JsonArrayBuilder teamArrayBuilder = Json.createArrayBuilder();
        teams.forEach(t -> {
            teamArrayBuilder.add(buildTeamJsonObject(t));
        });
        return Response.ok(teamArrayBuilder.build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        return Response.ok(repository.delete(id)).build();
    }
}
