import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Competitor} from '../model/competitor.model';

export interface DialogData {
  competitors: Competitor[];
}


@Component({
  selector: 'app-tournament-dialog',
  templateUrl: './tournament-dialog.component.html',
  styleUrls: ['./tournament-dialog.component.scss']
})
export class TournamentDialogComponent{
  constructor(
    public dialogRef: MatDialogRef<TournamentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {
    console.log(data.competitors);
  }

  onNoClick(): void{
    this.dialogRef.close();
  }

  addCompetitor(competitor: Competitor): void{
    console.log(competitor);
    this.dialogRef.close({data: competitor});
  }
}
