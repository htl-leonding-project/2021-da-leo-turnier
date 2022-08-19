import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-tournament-overview-dialog',
  templateUrl: './tournament-overview-dialog.component.html',
  styleUrls: ['./tournament-overview-dialog.component.scss']
})
export class TournamentOverviewDialogComponent{

  constructor(public dialogRef: MatDialogRef<TournamentOverviewDialogComponent>) { }

  onNoClick(): void{
    this.dialogRef.close();
  }
}
