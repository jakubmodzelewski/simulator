import {NodeType} from "./node-type.enum";

export class Node {

  id: string;
  name: string;

  interfaces: string[];

  selected = false;
  type : NodeType;

  //Coordinates
  previousX = 0;
  previousY = 0;
  actualX = 0;
  actualY = 0;

  constructor() {
    this.interfaces = [];
  }

  public addInterface() {
    this.interfaces.push(this.id + "." + this.interfaces.length);
  }
}
