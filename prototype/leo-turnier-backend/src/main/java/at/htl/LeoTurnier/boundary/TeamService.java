package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Team;
import at.htl.LeoTurnier.repository.TeamRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/team")
public class TeamService {

    @Inject
    TeamRepository repository;

    @POST
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Team team, @Context UriInfo info) {
        team = repository.add(team);
        if (team == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", team.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, Team team, @Context UriInfo info) {
        team = repository.modify(id, team);
        if (team == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", team.getId()).build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
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
