package at.htl.LeoTurnier.boundary;


import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.repository.PlayerRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/player")
public class PlayerService {

    @Inject
    PlayerRepository repository;

    @POST
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Player player, @Context UriInfo info) {
        player = repository.add(player);
        if (player == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", player.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, Player player, @Context UriInfo info) {
        player = repository.modify(id, player);
        if (player == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", player.getId()).build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id, @QueryParam("teamId") Long teamId) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
        } else if (teamId != null) {
            return Response.ok(repository.getByTeamId(teamId)).build();
        }
        return Response.ok(repository.getAll()).build();
    }

    @DELETE
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id") Long id) {
        return Response.ok(repository.delete(id)).build();
    }
}
