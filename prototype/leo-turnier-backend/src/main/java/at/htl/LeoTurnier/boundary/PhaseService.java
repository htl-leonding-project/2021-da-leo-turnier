package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Phase;
import at.htl.LeoTurnier.repository.PhaseRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/phase")
public class PhaseService {

    @Inject
    PhaseRepository repository;

    @POST
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Phase phase, @Context UriInfo info) {
        phase = repository.add(phase);
        if (phase == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", phase.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, Phase phase, @Context UriInfo info) {
        phase = repository.modify(id, phase);
        if (phase == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", phase.getId()).build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id, @QueryParam("tournamentId") Long tournamentId, @QueryParam("groupNumber") Integer groupNumber) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
        } else if (tournamentId != null) {
            if (groupNumber != null) {
                return Response.ok(repository.getByTournamentGroup(tournamentId, groupNumber)).build();
            } else {
                return Response.ok(repository.getByTournamentId(tournamentId)).build();
            }
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
