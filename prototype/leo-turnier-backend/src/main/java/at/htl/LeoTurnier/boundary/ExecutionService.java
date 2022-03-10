package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.repository.ExecutionRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;

@Path("execution")
public class ExecutionService {

    @Inject
    ExecutionRepository repository;

    @GET
    @Path("startTournament")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament getById(@QueryParam("tournamentId") long id) {
        return repository.startTournament(id);
    }
}
