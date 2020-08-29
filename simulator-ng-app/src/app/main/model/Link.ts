import {Node} from "./Node";

export class Link {
  id: string;
  nodeA: Node;
  nodeB: Node;

  interfaceA: string;
  interfaceB: string;

  xA : number;
  yA : number;
  xB : number;
  yB : number;

  constructor(nodeA: Node, nodeB: Node, interfaceA: string, interfaceB: string) {
    this.id = nodeA.id;
    this.nodeA = nodeA;
    this.nodeB = nodeB;
    this.interfaceA = interfaceA;
    this.interfaceB = interfaceB;
    console.log("Link between " + nodeA.name + " and " + nodeB.name + " has been created");
  }
}
