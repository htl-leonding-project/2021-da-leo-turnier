import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Match} from '../model/match.model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private host = 'http://localhost:8080/api/match';

  constructor(private httpClient: HttpClient) {
  }

  async getMatches(id: string): Promise<Match[]>{
    return await this.httpClient.get<Match[]>(this.host + '?tournamentId=' + id).toPromise();
  }
}
