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

  constructor(private api: TournamentService, public router: Router, private elementRef: ElementRef) {

    // @ts-ignore
    const nav: Navigation = this.router.getCurrentNavigation();

    if (nav.extras && nav.extras.state && nav.extras.state.tournament && nav.extras.state.name) {
      console.log('ctor');
      console.log(JSON.parse(nav.extras.state.tournament));
      this.tournament = JSON.parse(nav.extras.state.tournament);
      this.name = nav.extras.state.name;
    }
  }

  ngAfterViewInit(): void {
    this.elementRef.nativeElement.ownerDocument
      .body.style.backgroundColor = '#2B3E50';
  }

  ngOnInit(): void {
    /*const phases: Phase[] = await this.api.getPhases(this.id);
    console.log('Phases');
    console.log(phases);

    const phaseLength = phases.length;

    for (const phase of phases){
      const round: NgttRound = {
        type: 'Winnerbracket',
        matches: Array()
      };
      if (phaseLength !== 0){
        round.type = 'Winnerbracket';
      }else {
        round.type = 'Final';
      }

      const nodes: Node[] = await this.api.getNodes(phase.id);
      console.log('Nodes');
      console.log(nodes);

      for (const node of nodes.sort((a, b) => (a.nodeNumber < b.nodeNumber) ? -1 : 1)){
        if ('match' in node){
          round.matches.push(
            {
              teams: [
                {name: node.match.competitor1.name, score: node.match.score1},
                {name: node.match.competitor2.name, score: node.match.score2}
              ]
            });
        }
        else{
          round.matches.push(
            {
              teams: [
                {name: 'Ausstehend', score: 0},
                {name: 'Ausstehend', score: 0}
              ]
            });
        }
      }
      this.tournament.rounds.push(round);
      console.log('Tournament');
      console.log(this.tournament);
    }*/
  }
}
