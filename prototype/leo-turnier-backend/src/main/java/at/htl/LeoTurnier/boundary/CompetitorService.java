package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Player;
import at.htl.LeoTurnier.entity.Team;
import at.htl.LeoTurnier.repository.CompetitorRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("competitor")
public class CompetitorService {

    @Inject
    CompetitorRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
        }
        return Response.ok(repository.getAll()).build();
    }
}
