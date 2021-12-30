package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import at.htl.LeoTurnier.repository.CompetitorRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("competitor")
public class CompetitorService {

    @Inject
    CompetitorRepository repository;

    @POST
    @Path("player")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlayer(Player player) {
        try {
            player = (Player) repository.add(player);
        } catch (IllegalArgumentException e) {
            return Response.notModified(e.getMessage()).build();
        }
        return Response.accepted(player).build();
    }

    @PUT
    @Path("player/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyPlayer(@PathParam("id") long id, Player player) {
        try {
            player = (Player) repository.modify(id, player);
        } catch (IllegalArgumentException e) {
            return Response.notModified(e.getMessage()).build();
        }
        return Response.accepted(player).build();
    }

    @POST
    @Path("team")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTeam(Team team) {
        try {
            team = (Team) repository.add(team);
        } catch (IllegalArgumentException e) {
            return Response.notModified(e.getMessage()).build();
        }
        return Response.accepted(team).build();
    }

    @PUT
    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyTeam(@PathParam("id") long id, Team team) {
        try {
            team = (Team) repository.modify(id, team);
        } catch (IllegalArgumentException e) {
            return Response.notModified(e.getMessage()).build();
        }
        return Response.accepted(team).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Competitor getById(@PathParam("id") long id) {
        return repository.findById(id);
    }

    @GET
    @Path("player")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Competitor> getAllPlayers() {
        return repository.getAll().stream()
                .filter(c -> c.getClass() == Player.class)
                .collect(Collectors.toList());
    }

    @GET
    @Path("team")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Competitor> getAllTeams() {
        return repository.getAll().stream()
                .filter(c -> c.getClass() == Team.class)
                .collect(Collectors.toList());
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Competitor> getAll() {
        return repository.getAll();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteElement(@PathParam("id") long id) {
        Competitor deleted;
        try {
            deleted = repository.delete(id);
        } catch (IllegalArgumentException e) {
            return Response.notModified(e.getMessage()).build();
        }
        return Response.accepted(deleted).build();
    }
}
