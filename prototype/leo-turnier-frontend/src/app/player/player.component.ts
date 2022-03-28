import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {PlayerService} from '../services/player.service';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  id = '';

  playerForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    birthdate: new FormControl(''),
  });

  private team: any;

  constructor(public api: PlayerService, public activatedRouter: ActivatedRoute, private router: Router) {
    // @ts-ignore
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    if (this.id !== '0') {
      const player = await this.api.getPlayer(this.id);
      console.log(player);
      this.playerForm.setValue({name: player.name, birthdate: player.birthdate});
      if ('team' in player){
        this.team = player.team;
      }
      else {
        this.team = null;
      }
      console.log(this.playerForm.value);
    }
  }

  async onSubmit(): Promise<void> {
    if (this.id !== '0') {
      // @ts-ignore
      await this.api.updatePlayer(this.id, this.playerForm.value, this.team);
    } else {
      await this.api.addPlayer(this.playerForm.value);
    }
    this.router.navigate(['players']).then(() => {
      window.location.reload();
    });
  }
}


