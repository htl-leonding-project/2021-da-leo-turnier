import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TeamService} from '../services/team.service';
import {PlayerService} from '../services/player.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {TournamentService} from '../services/tournament.service';
import {SportType} from '../model/sport-type.model';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {TournamentMode} from '../model/tournament-mode.model';
import {TournamentDialogComponent} from '../tournament-dialog/tournament-dialog.component';
import {Competitor} from '../model/competitor.model';

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.scss']
})
export class TournamentComponent implements OnInit {

  id = '';
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['name', 'actions'];
  sportTypes: SportType[] = Array(0);
  // @ts-ignore
  filteredSportTypes: Observable<SportType[]>;
  tournamentModes: TournamentMode[] = Array(0);

  tournamentForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    sportType: new FormControl(),
    tournamentMode: new FormControl('', Validators.required),
    startDate: new FormControl(''),
    endDate: new FormControl(''),
    competitors: new FormControl()
  });

  constructor(private teamApi: TeamService,
              private playerApi: PlayerService,
              private tournamentApi: TournamentService,
              private activatedRouter: ActivatedRoute,
              private router: Router,
              public dialog: MatDialog) {
    // @ts-ignore
    // tslint:disable-next-line:no-shadowed-variable
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    this.sportTypes = await this.tournamentApi.getSportTypes();
    this.tournamentModes = await this.tournamentApi.getTournamentModes();

    if (this.id !== '0') {
      const tournament = await this.tournamentApi.getTournament(this.id);
      console.log(tournament);
      // tslint:disable-next-line:max-line-length
      this.tournamentForm.setValue({name: tournament.name, sportType: tournament.sportType, tournamentMode: tournament.tournamentMode, startDate: tournament.startDate, endDate: tournament.endDate, competitors: tournament.competitors});
      this.dataSource.data = tournament.competitors;
      console.log(this.dataSource.data);
    }
    this.filteredSportTypes = this.tournamentForm.valueChanges.pipe(
      startWith(''),
      map(value => {
        // @ts-ignore
        return (typeof value === 'string' ? value : value.name);
      }),
      map(name => {
        // @ts-ignore
        return (name ? this._filter(name) : this.sportTypes.slice());
      }),
    );
  }

  displayFn(sportType: SportType): string {
    return sportType && sportType.name ? sportType.name : '';
  }

  private _filter(name: string): SportType[] {
    const filterValue = name.toLowerCase();

    return this.sportTypes.filter(sportType => sportType.name.toLowerCase().includes(filterValue));
  }

  async onSubmit(): Promise<void> {
    if (this.id !== '0') {
      await this.tournamentApi.updateTournament(this.id, this.tournamentForm.value);
    } else {
      await this.tournamentApi.addTournament(this.tournamentForm.value);
    }
    this.router.navigate(['tournaments']).then(() => {
      window.location.reload();
    });
  }

  async openDialog(): Promise<void> {
    const allCompetitors: Competitor[] = await this.tournamentApi.getCompetitors();
    console.log(allCompetitors);
    console.log(this.dataSource.data);

    const dialogRef = this.dialog.open(TournamentDialogComponent, {
      width: '250px',
      data: {competitors: this.getCompetitorsWithNoParticipation(allCompetitors)},
    });
    dialogRef.afterClosed().subscribe(async result => {
      console.log('The dialog was closed');
      console.log(result.data);
      await this.tournamentApi.addParticipationForCompetitor(+this.id, result.data.id);
      this.router.navigate(['tournaments/' + this.id]).then(() => {
        window.location.reload();
      });
    });
  }

  private getCompetitorsWithNoParticipation(allCompetitors: Competitor[]): Competitor[] {
    if (this.tournamentForm.value.competitors !== null){
      return allCompetitors.filter(item => !this.tournamentForm.value.competitors.includes(item));
    }
    else {
      return allCompetitors;
    }
  }
}
