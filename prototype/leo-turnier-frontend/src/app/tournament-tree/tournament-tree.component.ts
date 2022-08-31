import {AfterViewInit, Component, ElementRef, OnInit} from '@angular/core';
import {NgttTournament} from 'ng-tournament-tree';
import {TournamentService} from '../services/tournament.service';
import {Navigation, Router} from '@angular/router';


@Component({
  selector: 'app-tournament-tree',
  templateUrl: './tournament-tree.component.html',
  styleUrls: ['./tournament-tree.component.scss']
})
export class TournamentTreeComponent implements OnInit, AfterViewInit {

  // @ts-ignore
  public tournament: NgttTournament;

  public name = '';
  public type = '';

  constructor(private api: TournamentService, public router: Router, private elementRef: ElementRef) {

    // @ts-ignore
    const nav: Navigation = this.router.getCurrentNavigation();

    if (nav.extras && nav.extras.state && nav.extras.state.tournament && nav.extras.state.name) {
      this.tournament = JSON.parse(nav.extras.state.tournament);
      this.name = nav.extras.state.name;
      if (nav.extras.state.type){
        this.type = nav.extras.state.type;
      }
    }
  }

  ngAfterViewInit(): void {
    this.elementRef.nativeElement.ownerDocument
      .body.style.backgroundColor = '#2B3E50';
  }

  ngOnInit(): void {

  }
}
