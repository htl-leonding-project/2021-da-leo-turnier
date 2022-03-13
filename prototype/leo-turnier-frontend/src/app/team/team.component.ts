import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {MatTableDataSource} from '@angular/material/table';
import {TeamDialogComponent} from '../team-dialog/team-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {Player} from '../model/player.model';
import {TeamService} from '../services/team.service';
import {PlayerService} from '../services/player.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss']
})
export class TeamComponent implements OnInit {

  id = '';
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['name', 'actions'];

  teamForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    players: new FormControl()
  });

  constructor(public teamApi: TeamService,
              public playerApi: PlayerService,
              public activatedRouter: ActivatedRoute,
              private router: Router, public dialog: MatDialog) {
    // @ts-ignore
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    if (this.id !== '0') {
      const team = await this.teamApi.getTeam(this.id);
      console.log(team);
      this.teamForm.setValue({name: team.name, players: team.players});
      this.dataSource.data = team.players;
    }
  }

  async onSubmit(): Promise<void> {
    if (this.id !== '0') {
      await this.teamApi.updateTeam(this.id, this.teamForm.value);
    } else {
      await this.teamApi.addTeam(this.teamForm.value);
    }
    this.router.navigate(['team']).then(() => {
      window.location.reload();
    });
  }

  async openDialog(): Promise<void> {
    const allPlayers: Player[] = await this.playerApi.getPlayers();
    console.log(allPlayers);
    console.log(this.dataSource.data);


    const dialogRef = this.dialog.open(TeamDialogComponent, {
      width: '250px',
      data: {players: this.getPlayersNotInTeam(allPlayers)},
    });
    dialogRef.afterClosed().subscribe(async result => {
      console.log('The dialog was closed');
      result.data.team = await this.teamApi.getTeam(this.id);
      this.playerApi.updatePlayer(String(result.data.id), result.data);
      this.router.navigate(['team/' + this.id]).then(() => {
        window.location.reload();
      });
    });
  }

  private async getPlayersNotInTeam(allPlayers: Player[]): Promise<Player[]> {
    const playersNotInTeam: Player[] = Array(0);

    for (const player of allPlayers) {
      // @ts-ignore
      if (!this.dataSource.data.some((teamPlayer: Player) => teamPlayer.name === player.name)) {
        playersNotInTeam.push(player);
      }
    }
    console.log(playersNotInTeam);
    return playersNotInTeam;
  }

  public removePlayer(player: Player): void {
    // @ts-ignore
    player.team = null;
    this.playerApi.updatePlayer(String(player.id), player);
    this.router.navigate(['team/' + this.id]).then(() => {window.location.reload(); });
  }
}
