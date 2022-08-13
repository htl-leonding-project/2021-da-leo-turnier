import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Match} from '../model/match.model';
import {KeycloakService} from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private host = 'http://localhost:8080/api/match';

  constructor(private httpClient: HttpClient,
              private keycloakService: KeycloakService) {
  }

  async getMatches(id: string): Promise<Match[]>{
    return await this.httpClient.get<Match[]>(this.host + '?tournamentId=' + id).toPromise();
  }

  async getMatch(id: string): Promise<Match> {
    return await this.httpClient.get<Match>(this.host + '?id=' + id).toPromise();
  }

  async updateMatch(match: Match): Promise<void> {
    const authToken = this.keycloakService.getToken();
    const headers = {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`
    };
    await this.httpClient.put(this.host + '?id=' + match.id, JSON.stringify(match), {headers}).subscribe(
      data => console.log('success', data),
      error => console.log('oops', error)
    );
  }
}
