import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatTableDataSource} from '@angular/material/table';
import {MatchService} from '../services/match.service';

@Component({
  selector: 'app-match-overview',
  templateUrl: './match-overview.component.html',
  styleUrls: ['./match-overview.component.scss']
})
export class MatchOverviewComponent implements OnInit {
  id = '';
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['competitor1', 'score', 'competitor2', 'actions'];

  constructor(private activatedRouter: ActivatedRoute,
              public router: Router,
              private api: MatchService) {
    // @ts-ignore
    activatedRouter.paramMap.subscribe(map => this.id = map.get('id'));
  }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getMatches(this.id);
    console.log(this.dataSource.data);
  }

  navigateToSubmition(id: number): void {
    this.router.navigate(['match-submition/' + id + '/' + this.id]);
  }
}
