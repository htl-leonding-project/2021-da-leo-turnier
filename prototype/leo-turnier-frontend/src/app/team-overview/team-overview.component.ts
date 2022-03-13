import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {TeamOverviewDialogComponent} from '../team-overview-dialog/team-overview-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {TeamService} from '../services/team.service';

@Component({
  selector: 'app-team-overview',
  templateUrl: './team-overview.component.html',
  styleUrls: ['./team-overview.component.scss']
})
export class TeamOverviewComponent implements OnInit {

  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['id', 'name', 'players', 'actions'];

  constructor(private api: TeamService, public router: Router, public dialog: MatDialog) { }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getTeams();
  }


  openDialog(selectedPlayers: any): void{
    const dialogRef = this.dialog.open(TeamOverviewDialogComponent, {
      width: '250px',
      data: {players: selectedPlayers},
    });
    dialogRef.afterClosed().subscribe(() => {
      console.log('The dialog was closed');
    });
  }
}

