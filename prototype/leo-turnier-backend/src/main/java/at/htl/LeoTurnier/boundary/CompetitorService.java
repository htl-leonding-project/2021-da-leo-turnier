package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.repository.CompetitorRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("competitor")
public class CompetitorService {

    @Inject
    CompetitorRepository repository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id, @QueryParam("tournamentId") Long tournamentId) {
        if (id != null) {
            return Response.ok(repository.getById(id)).build();
        } else if (tournamentId != null) {
            return Response.ok(repository.getByTournamentId(tournamentId)).build();
        }
        return Response.ok(repository.getAll()).build();
    }
}
