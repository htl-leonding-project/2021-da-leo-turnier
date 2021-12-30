import { Component, OnInit } from '@angular/core';
import {ApiService} from '../services/api.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  id = '';

  playerForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    totalScore: new FormControl(''),
    birthdate: new FormControl(''),
  });

  constructor(public api: ApiService, public activatedRouter: ActivatedRoute, private router: Router) {
    // @ts-ignore
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    if (this.id !== '0') {
      const player = await this.api.getPlayer(this.id);
      console.log(player);
      this.playerForm.setValue({name: player.name, totalScore: player.totalScore, birthdate: null});
    }
  }

  onSubmit(): void {
    if (this.id !== '0'){
      this.api.updatePlayer(this.playerForm.value);
    }else {
      this.api.addPlayer(this.playerForm.value);
    }
    this.router.navigate(['players']).then(() => {window.location.reload()});
  }
}


