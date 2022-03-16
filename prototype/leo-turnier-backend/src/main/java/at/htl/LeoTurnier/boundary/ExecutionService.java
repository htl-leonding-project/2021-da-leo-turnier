package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.entity.Node;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.repository.ExecutionRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;

@Path("execution")
public class ExecutionService {

    @Inject
    ExecutionRepository repository;

    @GET
    @Path("startTournament")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startTournament(@QueryParam("tournamentId") long id) {
        return repository.startTournament(id);
    }

    @GET
    @Path("finishMatch")
    @Produces(MediaType.APPLICATION_JSON)
    public Match finishMatch(@QueryParam("nodeId") long nodeId) {
        return repository.finishMatch(nodeId);
    }
}
