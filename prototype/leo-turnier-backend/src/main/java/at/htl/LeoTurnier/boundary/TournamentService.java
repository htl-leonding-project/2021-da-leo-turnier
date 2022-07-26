package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.repository.PlayerRepository;
import at.htl.LeoTurnier.repository.TournamentRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/tournament")
public class TournamentService {

    @Inject
    TournamentRepository repository;

    @POST
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Tournament tournament, @Context UriInfo info) {
        tournament = repository.add(tournament);
        if (tournament == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", tournament.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, Tournament tournament, @Context UriInfo info) {
        tournament = repository.modify(id, tournament);
        if (tournament == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", tournament.getId()).build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id, @QueryParam("competitorId") Long competitorId) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
        } else if (competitorId != null) {
            return Response.ok(repository.getByCompetitorId(competitorId)).build();
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
