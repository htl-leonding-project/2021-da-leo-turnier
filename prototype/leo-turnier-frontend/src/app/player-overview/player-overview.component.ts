import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {PlayerService} from '../services/player.service';

@Component({
  selector: 'app-player-overview',
  templateUrl: './player-overview.component.html',
  styleUrls: ['./player-overview.component.scss']
})
export class PlayerOverviewComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['id', 'name', 'birthdate', 'team', 'actions'];


  constructor(private api: PlayerService, public router: Router) { }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getPlayers();
  }

   async deletePlayer(id: number): Promise<void> {
     if (confirm('Are you sure to delete this record ?')) {
       await this.api.deletePlayer(id);
       await new Promise(f => setTimeout(f, 10));
     }
     window.location.reload();
   }

  editPlayer(id: number): void {
    this.router.navigate(['player-detail'], {state: {data: id}});
  }
}
