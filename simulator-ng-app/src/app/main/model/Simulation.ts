import {Link} from "./Link";
import {Node} from "./Node";

export class Simulation {
  id : string;
  nodes : Node[];
  links : Link[];

  constructor() {
    this.nodes = [];
    this.links = [];
  }
}
