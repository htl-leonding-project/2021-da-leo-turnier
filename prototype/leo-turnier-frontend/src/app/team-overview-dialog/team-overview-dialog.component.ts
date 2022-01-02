import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Player} from '../model/player.model';

export interface DialogData {
  players: Player[];
}

@Component({
  selector: 'app-team-overview-dialog',
  templateUrl: './team-overview-dialog.component.html',
  styleUrls: ['./team-overview-dialog.component.scss']
})
export class TeamOverviewDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<TeamOverviewDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {
    console.log(data.players);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
