import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {TournamentService} from '../services/tournament.service';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {TournamentOverviewDialogComponent} from '../tournament-overview-dialog/tournament-overview-dialog.component';

@Component({
  selector: 'app-tournament-overview',
  templateUrl: './tournament-overview.component.html',
  styleUrls: ['./tournament-overview.component.scss']
})
export class TournamentOverviewComponent implements OnInit {

  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['id', 'name', 'startDate', 'endDate', 'sportType', 'mode', 'actions'];

  constructor(public api: TournamentService,
              private router: Router,
              public dialog: MatDialog) { }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getTournaments();
    console.log(this.dataSource.data);
  }

  async startTournament(id: string): Promise<void> {
    const tournament = await this.api.getTournament(id);
    if (tournament.competitors.length <= 2){
      const dialogRef = this.dialog.open(TournamentOverviewDialogComponent, {
        width: '650px'});
    }
    else if (confirm('Sind sie sicher dass sie dass Turnier starten möchten?' +
      'Danach können Sie es nicht mehr bearbeiten!')) {

      await this.api.startTournament(id);
      await new Promise(f => setTimeout(f, 10));
      window.location.reload();
    }
  }

  async deleteTournament(id: number): Promise<void> {
    if (confirm('Sind Sie sicher dass Sie dieses Turnier löschen möchten?')) {
      await this.api.deleteTournament(id);
      await new Promise(f => setTimeout(f, 10));
    }
    window.location.reload();
  }

  navigateToMatches(id: number): void{
    this.router.navigate(['/matches/' + id]);
  }
}
