package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.SportType;
import at.htl.LeoTurnier.repository.PlayerRepository;
import at.htl.LeoTurnier.repository.SportTypeRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sportType")
public class SportTypeService {

    @Inject
    SportTypeRepository repository;

    @POST
    @RolesAllowed({"Organizer"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(SportType sportType, @Context UriInfo info) {
        sportType = repository.add(sportType);
        if (sportType == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", sportType.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, SportType sportType, @Context UriInfo info) {
        sportType = repository.modify(id, sportType);
        if (sportType == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", sportType.getId()).build()).build();
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
