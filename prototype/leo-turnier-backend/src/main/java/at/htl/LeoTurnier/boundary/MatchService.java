package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.repository.MatchRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/match")
public class MatchService {

    @Inject
    MatchRepository repository;

    @POST
    @RolesAllowed({"Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Match match, @Context UriInfo info) {
        match = repository.add(match);
        if (match == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", match.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, Match match, @Context UriInfo info) {
        match = repository.modify(id, match);
        if (match == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", match.getId()).build()).build();
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
    @RolesAllowed({"Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id") Long id) {
        return Response.ok(repository.delete(id)).build();
    }
}
