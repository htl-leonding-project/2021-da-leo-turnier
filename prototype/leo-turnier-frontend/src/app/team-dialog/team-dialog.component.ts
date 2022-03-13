import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogData} from '../team-overview-dialog/team-overview-dialog.component';
import {Player} from '../model/player.model';

@Component({
  selector: 'app-team-dialog',
  templateUrl: './team-dialog.component.html',
  styleUrls: ['./team-dialog.component.scss']
})
export class TeamDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<TeamDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {
    console.log(data.players);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  addPlayerToTeam(player: Player): void {
    this.dialogRef.close({data: player});
  }
}
