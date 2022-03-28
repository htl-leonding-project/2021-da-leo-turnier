import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {TournamentService} from '../services/tournament.service';

@Component({
  selector: 'app-tournament-overview',
  templateUrl: './tournament-overview.component.html',
  styleUrls: ['./tournament-overview.component.scss']
})
export class TournamentOverviewComponent implements OnInit {

  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['id', 'name', 'startDate', 'endDate', 'sportType', 'mode', 'actions'];

  constructor(public api: TournamentService) { }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getTournaments();
  }

  async startTournament(id: number): Promise<void> {
    if (confirm('Sind sie sicher dass sie dass Turnier starten möchten?' +
      'Danach können Sie es nicht mehr bearbeiten!')) {

      await this.api.startTournament(id);
      await new Promise(f => setTimeout(f, 10));
    }
    window.location.reload();
  }

  async deleteTournament(id: number): Promise<void> {
    if (confirm('Sind Sie sicher dass Sie dieses Turnier löschen möchten?')) {
      await this.api.deleteTournament(id);
      await new Promise(f => setTimeout(f, 10));
    }
    window.location.reload();
  }
}
