import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {Tournament} from '../model/tournament.model';
import {Competitor} from '../model/competitor.model';
import {Phase} from '../model/phase.model';
import {Node} from '../model/node.model';

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  private host = 'http://localhost:8080/api/';

  constructor(private httpClient: HttpClient, public datePipe: DatePipe) {
  }

  async getTournaments(): Promise<Tournament[]>{
    const tournaments: Tournament[] = await this.httpClient.get<Tournament[]>(this.host + 'tournament').toPromise();

    for (const tournament of tournaments) {
      if ((await this.httpClient.get<any>(this.host + 'phase?tournamentId=' + tournament.id).toPromise()) !== null) {
        tournament.isTournamentRunning = true;
      }
      else {
        tournament.isTournamentRunning = false;
      }
    }

    console.log(tournaments);

    return tournaments;
  }

  async getTournament(id: string): Promise<Tournament>{
    const tournament: Tournament = await this.httpClient.get<Tournament>(this.host + 'tournament?id=' + id).toPromise();
    tournament.competitors = await this.httpClient
      .get<Competitor[]>(this.host + 'participation?tournamentId=' + id)
      .toPromise();

    console.log(tournament);

    return tournament;
  }

  async startTournament(id: number): Promise<void> {
    await this.httpClient.get(this.host + 'execution/startTournament?tournamentId=' + id).toPromise();
  }

  async getRunningTournaments(): Promise<Tournament[]> {
    const runningTournaments: Tournament[] = [];
    for (const tournament of await this.getTournaments()) {
      if (tournament.isTournamentRunning){
        runningTournaments.push(tournament);
      }
    }
    return runningTournaments;
  }

  async getPhases(id: string): Promise<Phase[]> {
    return await this.httpClient.get<Phase[]>(this.host +  'phase?tournamentId=' + id).toPromise();
  }

  async getNodes(phaseId: number): Promise<Node[]> {
    return await this.httpClient.get<Node[]>(this.host + 'node?phaseId=' + phaseId).toPromise();
  }
}
