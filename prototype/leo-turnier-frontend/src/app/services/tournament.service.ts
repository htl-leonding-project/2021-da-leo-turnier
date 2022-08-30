import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {Tournament} from '../model/tournament.model';
import {Competitor} from '../model/competitor.model';
import {Phase} from '../model/phase.model';
import {Node} from '../model/node.model';
import {SportType} from '../model/sport-type.model';
import {TournamentMode} from '../model/tournament-mode.model';
import {TournamentDTO} from '../model/tournamentDTO.model';
import {KeycloakService} from 'keycloak-angular';
import {Participation} from '../model/participation.model';

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  private host = 'http://localhost:8080/api/';

  constructor(private httpClient: HttpClient, public datePipe: DatePipe, private keycloakService: KeycloakService) {
  }

  async getTournaments(): Promise<Tournament[]>{
    const tournaments: Tournament[] = await this.httpClient.get<Tournament[]>(this.host + 'tournament').toPromise();

    console.log(tournaments);

    for (const tournament of tournaments) {
      if ((await this.httpClient.get<any>(this.host + 'phase?tournamentId=' + tournament.id).toPromise()).length !== 0) {
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

  async addTournament(value: any): Promise<void> {
    if (value.sportType == null){
      value.sportType = '';
    }

    // @ts-ignore
    // tslint:disable-next-line:max-line-length
    const tournament = new TournamentDTO(null, value.name, this.datePipe.transform(value.startDate, 'yyyy-MM-dd'), this.datePipe.transform(value.endDate, 'yyyy-MM-dd'), new SportType(value.sportType), value.tournamentMode);
    console.log(JSON.stringify(tournament));

    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    console.log(headers);

    await this.httpClient.post(this.host + 'tournament', JSON.stringify(tournament), {headers}).subscribe(
      data => console.log('success', data),
      // tslint:disable-next-line:no-shadowed-variable
      error => console.log('oops', error)
    );

    await new Promise(f => setTimeout(f, 10));
  }

  async updateTournament(id: string, value: any): Promise<void> {
    if (value.sportType == null){
      value.sportType = '';
    }
    // @ts-ignore
    // tslint:disable-next-line:max-line-length
    const tournament = new TournamentDTO(+id, value.name, this.datePipe.transform(value.startDate, 'yyyy-MM-dd'), this.datePipe.transform(value.endDate, 'yyyy-MM-dd'), value.sportType, value.tournamentMode);
    console.log(JSON.stringify(tournament));

    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    console.log(headers);

    await this.httpClient.put(this.host + 'tournament?id=' + id, JSON.stringify(tournament), {headers}).subscribe(
      data => console.log('success', data),
      // tslint:disable-next-line:no-shadowed-variable
      error => console.log('oops', error)
    );
  }

  async startTournament(id: string): Promise<void> {
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    await this.httpClient.get(this.host + 'execution/startTournament?tournamentId=' + id, {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
    await new Promise(f => setTimeout(f, 10));
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

  async getSportTypes(): Promise<SportType[]> {
    return  await this.httpClient.get<SportType[]>(this.host + 'sportType').toPromise();
  }

  async getTournamentModes(): Promise<TournamentMode[]> {
    return await this.httpClient.get<TournamentMode[]>(this.host + 'tournamentMode').toPromise();
  }

  async getCompetitors(): Promise<Competitor[]> {
    return await this.httpClient.get<Competitor[]>(this.host + 'competitor').toPromise();
  }

  async addParticipationForCompetitor(tournamentId: number, competitorId: number): Promise<void> {
    console.log(this.host + 'participation?tournamentId=' + tournamentId + '&competitorId=' + competitorId);

    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    console.log(headers);

    this.httpClient.post(this.host + 'participation?tournamentId=' + tournamentId + '&competitorId=' + competitorId, null).subscribe(
      (response) => {
        console.log(response);
      }
    );
  }

  async deleteTournament(id: number): Promise<void> {
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    console.log(headers);

    this.httpClient.delete(this.host + 'tournament?id=' + id, {headers}).subscribe(
      data => console.log('succes', data),
      // tslint:disable-next-line:no-shadowed-variable
      error => console.log('oops', error)
    );
  }

  async deleteParticipation(tournamentId: string, compId: number): Promise<void> {
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    console.log(headers);

    console.log(tournamentId + ' ' + compId);

    this.httpClient.delete(this.host + 'participation?tournamentId=' + tournamentId + '&competitorId=' + compId, {headers}).subscribe(
      data => console.log('succes', data),
      // tslint:disable-next-line:no-shadowed-variable
      error => console.log('oops', error)
    );
  }

  async getPlacements(id: string): Promise<Participation[]> {
    console.log(await this.httpClient.get<Participation[]>(this.host + 'participation?tournamentId=' + id).toPromise());

    return await this.httpClient.get<Participation[]>(this.host + 'participation?tournamentId=' + id).toPromise();
  }

  async startTieBreakers(id: string): Promise<void> {
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };

    await this.httpClient.get(this.host + 'execution/startTieBreakers?tournamentId=' + id, {headers}).subscribe(
      data => console.log('succes', data),
      // tslint:disable-next-line:no-shadowed-variable
      error => console.log('oops', error)
    );
  }
}
