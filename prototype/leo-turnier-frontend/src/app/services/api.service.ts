import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Player} from '../model/player.model';
import {Team} from '../model/team.model';
@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private host = 'http://localhost:8080/api/';

  constructor(private httpClient: HttpClient) {
  }

  async getPlayers(): Promise<Player[]>{
    return this.httpClient.get<Player[]>(this.host + 'player').toPromise();
  }

  async getPlayer(id: string): Promise<Player> {
    return this.httpClient.get<Player>(this.host + 'player/' + id).toPromise();
  }

  async deletePlayer(id: number): Promise<void> {
    this.httpClient.delete(this.host + id).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }

  async addPlayer(value: any): Promise<void> {
    // @ts-ignore
    // const player = new Player(null, value.name, value.totalScore, null, value.birthdate, null);
    const player = new Player(null, value.name, 0, null, null, null);
    console.log(JSON.stringify(player));
    const headers = { 'content-type': 'application/json'};
    await this.httpClient.post(this.host + 'player', JSON.stringify(player), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }

  async updatePlayer(value: any): Promise<void>{
    // @ts-ignore
    const player = new Player(value.id, value.name, value.totalScore, null, value.birthdate, null);
    const headers = { 'content-type': 'application/json'};
    await this.httpClient.post(this.host + 'player' + value.id, JSON.stringify(player), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }

  async getTeams(): Promise<Team[]>{
    // return this.httpClient.get<Team[]>(this.host + 'team').toPromise();
    // Mocking
    console.log(this.httpClient.get<Team[]>('http://wovg1.mocklab.io/team').toPromise());
    return this.httpClient.get<Team[]>('http://wovg1.mocklab.io/team').toPromise();
  }
}
