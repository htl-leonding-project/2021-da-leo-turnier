import { Component, OnInit } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {TournamentService} from '../services/tournament.service';
import {Router} from '@angular/router';
import {Phase} from '../model/phase.model';
import {NgttRound, NgttTournament} from 'ng-tournament-tree';
import {Node} from '../model/node.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['name', 'sportType', 'startDate', 'endDate', 'actions'];

  constructor(private api: TournamentService, public router: Router) { }

  async ngOnInit(): Promise<void> {
    this.dataSource.data = await this.api.getRunningTournaments();
  }

  async showVisuals(element: any): Promise<void> {
    if (element.tournamentMode.name === 'Elimination'){
      await this.showTree(element);
    }
    if (element.tournamentMode.name === 'Round Robin') {
      console.log(element);
      await this.router.navigate(['tournament-table/' + element.id]);
    }
  }

  async showTree(element: any): Promise<void> {
    const tournament: NgttTournament = {rounds: Array()};

    const phases: Phase[] = await this.api.getPhases(element.id.toString());
    console.log('Phases');
    console.log(phases);

    let phaseLength = phases.length;

    for (const phase of phases){
      phaseLength--;
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
        console.log(node);
        if ('match' in node) {
          if ('competitor1' in node.match && 'competitor2' in node.match) {
            round.matches.push(
              {
                teams: [
                  {name: node.match.competitor1.name, score: node.match.score1},
                  {name: node.match.competitor2.name, score: node.match.score2}
                ]
              });
          } else if ('competitor1' in node.match) {
            round.matches.push(
              {
                teams: [
                  // @ts-ignore
                  {name: node.match.competitor1.name, score: 0},
                  {name: 'Ausstehend', score: 0},
                ]
              });
          } else if ('competitor2' in node.match) {
            round.matches.push(
              {
                teams: [
                  {name: 'Ausstehend', score: 0},
                  // @ts-ignore
                  {name: node.match.competitor2.name, score: 0}
                ]
              });
          }
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
      tournament.rounds.push(round);
      console.log('Tournament');
      console.log(tournament);
    }
    await this.router.navigate(['tournament-tree'],
      { state: {tournament: JSON.stringify(tournament), name: element.name, type: element.sportType.name}});
  }
}
