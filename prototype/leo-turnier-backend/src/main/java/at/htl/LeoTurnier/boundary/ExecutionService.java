package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Competitor;
import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.entity.Node;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.repository.ExecutionRepository;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;

@Path("execution")
public class ExecutionService {

    @Inject
    ExecutionRepository repository;

    @GET
    @RolesAllowed({"Organizer"})
    @Path("startTournament")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startTournament(@QueryParam("tournamentId") Long id, @QueryParam("numOfGroups") Integer numOfGroups) {
        return repository.startTournament(id, numOfGroups);
    }

    @GET
    @RolesAllowed({"Organizer"})
    @Path("startTieBreakers")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startTieBreakers(@QueryParam("tournamentId") Long id) {
        return repository.startTieBreakers(id);
    }

    @GET
    @RolesAllowed({"Organizer"})
    @Path("startKOPhase")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startKOPhase(@QueryParam("tournamentId") Long id, @QueryParam("numOfGroups") Integer promotedPerGroup) {
        return repository.startKOPhase(id, promotedPerGroup);
    }

    @GET
    @RolesAllowed({"Organizer"})
    @Path("finishMatch")
    @Produces(MediaType.APPLICATION_JSON)
    public Match finishMatch(@QueryParam("nodeId") Long nodeId) {
        return repository.finishMatch(nodeId);
    }

    @GET
    @Path("startTieBreakers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rankCompetitors(@QueryParam("tournamentId") Long id) {
        repository.rankCompetitors(id);
        return Response.ok("Competitors ranked").build();
    }

    @GET
    @Path("startTieBreakers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearTournament(@QueryParam("tournamentId") Long id) {
        repository.clearTournament(id);
        return Response.ok("Tournament cleared").build();
    }
}
