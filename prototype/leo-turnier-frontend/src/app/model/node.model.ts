import {Match} from './match.model';

export class Node {
  constructor(public id: number,
              public nodeNumber: number,
              public match: Match){
  }
}
