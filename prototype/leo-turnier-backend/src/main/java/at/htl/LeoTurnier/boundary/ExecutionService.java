package at.htl.LeoTurnier.boundary;

import at.htl.LeoTurnier.entity.Match;
import at.htl.LeoTurnier.entity.Tournament;
import at.htl.LeoTurnier.execution.Execution;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("execution")
public class ExecutionService {

    @Inject
    Execution repository;

    @GET
    @RolesAllowed({"tournament-organizer"})
    @Path("startTournament")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startTournament(@QueryParam("tournamentId") Long id, @QueryParam("numOfGroups") Integer numOfGroups) {
        return repository.startTournament(id, numOfGroups);
    }

    @GET
    @RolesAllowed({"tournament-organizer"})
    @Path("startTieBreakers")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startTieBreakers(@QueryParam("tournamentId") Long id) {
        return repository.startTieBreakers(id);
    }

    @GET
    @RolesAllowed({"tournament-organizer"})
    @Path("startKOPhase")
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament startKOPhase(@QueryParam("tournamentId") Long id, @QueryParam("numOfGroups") Integer promotedPerGroup) {
        return repository.startKOPhase(id, promotedPerGroup);
    }

    @GET
    @RolesAllowed({"tournament-organizer"})
    @Path("finishMatch")
    @Produces(MediaType.APPLICATION_JSON)
    public Match finishMatch(@QueryParam("matchId") Long matchId) {
        return repository.finishMatch(matchId);
    }

    @GET
    @Path("rankCompetitors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rankCompetitors(@QueryParam("tournamentId") Long id) {
        repository.rankCompetitors(id);
        return Response.ok("Competitors ranked").build();
    }

    @GET
    @RolesAllowed({"tournament-organizer"})
    @Path("clearTournament")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearTournament(@QueryParam("tournamentId") Long id) {
        repository.clearTournament(id);
        return Response.ok("Tournament cleared").build();
    }
}
