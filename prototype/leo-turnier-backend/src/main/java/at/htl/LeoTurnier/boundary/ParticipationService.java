package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Participation;
import at.htl.LeoTurnier.repository.ParticipationRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/participation")
public class ParticipationService {

    @Inject
    ParticipationRepository repository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId, @Context UriInfo info) {
        Participation participation = repository.add(tournamentId, competitorId);
        if (participation == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("t", tournamentId)
                .queryParam("c", competitorId)
                .build()).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId, Participation participation, @Context UriInfo info) {
        participation = repository.modify(tournamentId, competitorId, participation.getPlacement());
        if (participation == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("t", tournamentId)
                .queryParam("c", competitorId)
                .build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId) {
        return Response.ok(repository.getById(tournamentId, competitorId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTournamentId(@QueryParam("tournamentId") Long tournamentId) {
        return Response.ok(repository.getCompetitorsByTournament(tournamentId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCompetitorId(@QueryParam("competitorId") Long competitorId) {
        return Response.ok(repository.getTournamentsByCompetitor(competitorId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(repository.getAll()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId) {
        return Response.ok(repository.delete(tournamentId, competitorId)).build();
    }
}