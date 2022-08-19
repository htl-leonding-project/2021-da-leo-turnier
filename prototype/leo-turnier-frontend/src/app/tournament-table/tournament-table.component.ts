import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TournamentService} from '../services/tournament.service';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-tournament-table',
  templateUrl: './tournament-table.component.html',
  styleUrls: ['./tournament-table.component.scss']
})
export class TournamentTableComponent implements OnInit {
  public tournament: any;
  id = '';
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['competitor', 'placement'];

  constructor(public activatedRouter: ActivatedRoute, private api: TournamentService) {
    // @ts-ignore
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    this.tournament = await this.api.getTournament(this.id);
    const placements = await this.api.getPlacements(this.id);
    this.dataSource.data = placements.sort((n1, n2) => n1.placement - n2.placement);
    console.log(this.dataSource.data);
    console.log(this.tournament);
  }

}
