package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Node;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.repository.NodeRepository;
import at.htl.LeoTurnier.repository.PlayerRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/node")
public class NodeService {

    @Inject
    NodeRepository repository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Node node, @Context UriInfo info) {
        node = repository.add(node);
        if (node == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", node.getId()).build()).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modify(@QueryParam("id") Long id, Node node, @Context UriInfo info) {
        node = repository.modify(id, node);
        if (node == null) {
            return Response.status(204).build();
        }
        return Response.created(info
                .getAbsolutePathBuilder()
                .queryParam("id", node.getId()).build()).build();
    }

    @GET
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id, @QueryParam("phaseId") Long phaseId) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
        } else if (phaseId != null) {
            return Response.ok(repository.getByPhaseId(phaseId)).build();
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
