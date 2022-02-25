package at.htl.LeoTurnier.boundary;


import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import at.htl.LeoTurnier.repository.PlayerRepository;

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

@Path("/player")
public class PlayerService {

    @Inject
    PlayerRepository repository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Player player, @Context UriInfo info) {
        player = repository.add(player);
        if (player == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .path(Long.toString(player.getId())).build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@PathParam("id") long id, Player player, @Context UriInfo info) {
        player = repository.modify(id, player);
        if (player == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .path(Long.toString(player.getId())).build()).build();
    }

    private JsonObject buildPlayerJsonObject(Player player) {
        JsonObjectBuilder playerBuilder = Json.createObjectBuilder();
        playerBuilder.add("id", player.getId());
        playerBuilder.add("name", player.getName());
        playerBuilder.add("seed", player.getSeed());
        playerBuilder.add("birthdate", player.getBirthdate().toString());
        JsonObjectBuilder teamBuilder = Json.createObjectBuilder();
        teamBuilder.add("id", player.getTeam().getId());
        teamBuilder.add("name", player.getTeam().getName());
        teamBuilder.add("seed", player.getTeam().getSeed());
        playerBuilder.add("team", teamBuilder);
        return teamBuilder.build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        return Response.ok(buildPlayerJsonObject(repository.getById(id))).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Player> players = repository.getAll();
        JsonArrayBuilder playerArrayBuilder = Json.createArrayBuilder();
        players.forEach(t -> {
            playerArrayBuilder.add(buildPlayerJsonObject(t));
        });
        return Response.ok(playerArrayBuilder.build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        return Response.ok(repository.delete(id)).build();
    }
}
