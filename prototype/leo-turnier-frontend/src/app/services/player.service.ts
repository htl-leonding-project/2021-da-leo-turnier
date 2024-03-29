import {Injectable} from '@angular/core';
import {Player} from '../model/player.model';
import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {KeycloakService} from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  private host = 'http://localhost:8080/api/player';

  constructor(private httpClient: HttpClient, public datePipe: DatePipe, private keycloakService: KeycloakService) {
  }

  async getPlayers(): Promise<Player[]>{
    return this.httpClient.get<Player[]>(this.host).toPromise();

    // Mocking
    // return this.httpClient.get<Player[]>('http://wovg1.mocklab.io/player').toPromise();
  }

  async getPlayer(id: string): Promise<Player> {
    return await this.httpClient.get<Player>(this.host + '?id=' + id).toPromise();
  }

  async addPlayer(value: any): Promise<void> {
    // @ts-ignore
    const player = new Player(null, value.name, this.datePipe.transform(value.birthdate, 'yyyy-MM-dd'), null);
    console.log(JSON.stringify(player));

    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    console.log(headers);
    await this.httpClient.post(this.host, JSON.stringify(player), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );

    await new Promise(f => setTimeout(f, 100));
  }

  async updatePlayer(id: string, value: any, team: any): Promise<void>{
    // @ts-ignore
    const player = new Player(value.id, value.name, this.datePipe.transform(value.birthdate, 'yyyy-MM-dd'), team);
    console.log(JSON.stringify(player));
    const headers = { 'content-type': 'application/json'};
    await this.httpClient.put(this.host + '?id=' + id, JSON.stringify(player), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }

  async deletePlayer(id: number): Promise<void> {
    this.httpClient.delete(this.host + '?id=' + id).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }
}
