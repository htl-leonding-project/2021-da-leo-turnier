package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Phase;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.repository.PhaseRepository;
import at.htl.LeoTurnier.repository.PlayerRepository;

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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Phase phase, @Context UriInfo info) {
        phase = repository.add(phase);
        if (phase == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .path(Long.toString(phase.getId())).build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@PathParam("id") long id, Phase phase, @Context UriInfo info) {
        phase = repository.modify(id, phase);
        if (phase == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .path(Long.toString(phase.getId())).build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        return Response.ok(repository.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(repository.getAll()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        return Response.ok(repository.delete(id)).build();
    }
}
