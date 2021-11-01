import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Player} from '../model/player.model';
import {Team} from '../model/team.model';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private host = 'http://localhost:8080/api/competitor/';

  constructor(private httpClient: HttpClient) {
  }

  async getPlayers(): Promise<Player[]>{
    return this.httpClient.get<Player[]>(this.host + 'player').toPromise();
  }

  async getTeams(): Promise<Team[]>{
    return this.httpClient.get<Team[]>(this.host + 'team').toPromise();
  }
}
