package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Participation;
import at.htl.LeoTurnier.repository.ParticipationRepository;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/participation")
public class ParticipationService {

    @Inject
    ParticipationRepository repository;

    @POST
    @RolesAllowed({"tournament-organizer"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId, @Context UriInfo info) {
        Participation participation = repository.add(tournamentId, competitorId);
        if (participation == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("tournamentId", tournamentId)
                .queryParam("competitorId", competitorId)
                .build()).build();
    }

    @PUT
    @RolesAllowed({"tournament-organizer"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId, Participation participation, @Context UriInfo info) {
        if (repository.getById(tournamentId, competitorId) == null ||
                participation.getPlacement() == null && participation.getSeed() == null) {
            return Response.status(204).build();
        }
        if (participation.getPlacement() != null) {
            repository.modifyPlacement(tournamentId, competitorId, participation.getPlacement());
        }
        if (participation.getSeed() != null) {
            repository.modifySeed(tournamentId, competitorId, participation.getSeed());
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("t", tournamentId)
                .queryParam("c", competitorId)
                .build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId) {
        if (tournamentId != null && competitorId != null) {
            return Response.ok(repository.getById(tournamentId, competitorId)).build();
        } else if (tournamentId != null) {
            return Response.ok(repository.getByTournamentId(tournamentId)).build();
        } else if (competitorId != null) {
            return Response.ok(repository.getByCompetitorId(competitorId)).build();
        }
        return Response.ok(repository.getAll()).build();
    }

    @DELETE
    @RolesAllowed({"tournament-organizer"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("tournamentId") Long tournamentId, @QueryParam("competitorId") Long competitorId) {
        return Response.ok(repository.delete(tournamentId, competitorId)).build();
    }

    @GET
    @RolesAllowed({"tournament-organizer"})
    @Path("seedCompetitors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seedCompetitors(@QueryParam("tournamentId") Long tournamentId) {
        if (tournamentId != null) {
            return Response.ok(repository.seedCompetitors(tournamentId)).build();
        }
        return Response.status(204).build();
    }

    @POST
    @RolesAllowed({"tournament-organizer"})
    @Path("seedCompetitors")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response orderCompetitors(@QueryParam("tournamentId") Long tournamentId, List<Competitor> competitors) {
        if (tournamentId != null) {
            return Response.ok(repository.orderCompetitors(tournamentId, competitors)).build();
        }
        return Response.status(204).build();
    }
}
