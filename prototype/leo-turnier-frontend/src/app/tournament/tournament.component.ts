import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TeamService} from '../services/team.service';
import {PlayerService} from '../services/player.service';
import {ActivatedRoute} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {TournamentService} from '../services/tournament.service';

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.scss']
})
export class TournamentComponent implements OnInit {

  id = '';
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['name', 'actions'];

  tournamentForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    startDate: new FormControl(''),
    endDate: new FormControl(''),
    competitors: new FormControl()
  });

  constructor(public teamApi: TeamService,
              public playerApi: PlayerService,
              public tournamentApi: TournamentService,
              public activatedRouter: ActivatedRoute,
              public dialog: MatDialog) {
    // @ts-ignore
    // tslint:disable-next-line:no-shadowed-variable
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    if (this.id !== '0') {
      const tournament = await this.tournamentApi.getTournament(this.id);
      console.log(tournament);
      // tslint:disable-next-line:max-line-length
      this.tournamentForm.setValue({name: tournament.name, startDate: tournament.startDate, endDate: tournament.endDate, competitors: tournament.competitors});
      this.dataSource.data = tournament.competitors;
    }
  }

  onSubmit(): void {

  }
}
