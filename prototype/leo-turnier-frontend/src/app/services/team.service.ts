import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {Team} from '../model/team.model';
import {Player} from '../model/player.model';
import {Injectable} from '@angular/core';
import {KeycloakService} from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private host = 'http://localhost:8080/api/team';

  constructor(private httpClient: HttpClient, public datePipe: DatePipe, private keycloakService: KeycloakService) {
  }

  async getTeams(): Promise<Team[]>{
    const teams: Team[] = await this.httpClient.get<Team[]>(this.host).toPromise();
    for (const team of teams){
      team.players = await this.httpClient.get<Player[]>('http://localhost:8080/api/player?teamId=' + team.id).toPromise();
    }
    return teams;

    // Mocking
    // console.log(this.httpClient.get<Team[]>('http://wovg1.mocklab.io/team').toPromise());
    // return this.httpClient.get<Team[]>('http://wovg1.mocklab.io/team').toPromise();
  }

  async getTeam(id: string): Promise<Team> {
    const team: Team = await this.httpClient.get<Team>(this.host + '?id=' + id ).toPromise();
    team.players = await this.httpClient.get<Player[]>('http://localhost:8080/api/player?teamId=' + team.id).toPromise();
    return team;

    // Mocking
    // return this.httpClient.get<Team>('http://wovg1.mocklab.io/team/example').toPromise();
  }

  async addTeam(value: any): Promise<void> {
    // @ts-ignore
    const team = new Team(null, value.name, value.players);
    console.log(JSON.stringify(team));
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    await this.httpClient.post(this.host, JSON.stringify(team), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }

  async updateTeam(id: string, value: any): Promise<void> {
    // @ts-ignore
    const team = new Player(value.id, value.name, value.players);
    console.log(JSON.stringify(team));
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    await this.httpClient.put(this.host + '?id=' + id, JSON.stringify(team), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }

  async deleteTeam(id: number): Promise<void> {
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    this.httpClient.delete(this.host + '?id=' + id, {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }
}
