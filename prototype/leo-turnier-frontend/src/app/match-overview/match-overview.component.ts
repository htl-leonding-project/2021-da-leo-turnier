import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatTableDataSource} from '@angular/material/table';
import {MatchService} from '../services/match.service';
import {TournamentService} from '../services/tournament.service';

@Component({
  selector: 'app-match-overview',
  templateUrl: './match-overview.component.html',
  styleUrls: ['./match-overview.component.scss']
})
export class MatchOverviewComponent implements OnInit {
  id = '';
  tieBreakers = false;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['competitor1', 'score', 'competitor2', 'actions'];

  constructor(private activatedRouter: ActivatedRoute,
              public router: Router,
              private api: MatchService,
              private tournamentApi: TournamentService) {
    // @ts-ignore
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getMatches(this.id);
    const tournament = await this.tournamentApi.getTournament(this.id);
    console.log(this.dataSource.data);
    console.log(this.dataSource.data.some(match => // @ts-ignore
      // tslint:disable-next-line:no-unused-expression
      match.finished === true));
    console.log(tournament);
    if (tournament.tournamentMode.name === 'Round Robin'){
      if (!tournament.isFinished){
        if (!this.dataSource.data.some(match => // @ts-ignore
          // tslint:disable-next-line:no-unused-expression
          match.finished === false)){
          console.log('in');
          this.tieBreakers = true;
        }
      }
    }
  }




  navigateToSubmition(id: number): void {
    this.router.navigate(['match-submission/' + id + '/' + this.id]);
  }

  async startTieBreakers(): Promise<void> {
    await this.tournamentApi.startTieBreakers(this.id);
    window.location.reload();
  }
}
