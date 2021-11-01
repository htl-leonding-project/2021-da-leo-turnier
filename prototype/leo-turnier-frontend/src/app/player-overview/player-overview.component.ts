import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {ApiService} from '../services/api.service';

@Component({
  selector: 'app-competitor-overview',
  templateUrl: './player-overview.component.html',
  styleUrls: ['./player-overview.component.scss']
})
export class PlayerOverviewComponent implements OnInit {
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['id', 'name', 'totalScore', 'birthdate', 'team'];


  constructor(private api: ApiService) { }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getPlayers();
  }

}
