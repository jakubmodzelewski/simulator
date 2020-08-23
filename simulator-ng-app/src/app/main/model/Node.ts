import {NodeType} from "./node-type.enum";

export class Node {

  id: string;
  name: string;
  loopback: string;

  interfaces: string[];
  routingTable : Map<string, string>;

  selected = false;
  type : NodeType;

  //Coordinates
  previousX = 0;
  previousY = 0;
  actualX = 0;
  actualY = 0;

  constructor() {
    this.interfaces = [];
    this.routingTable = new Map<string, string>();
  }

  public addInterface() {
    this.interfaces.push(this.id + "." + this.interfaces.length);
  }

  getRoutingTableAsPairTable() {
    let table = [];
    for(let pair of this.routingTable) {
      table.push(pair[0], pair[1]);
    }
    return table;
  }
}
