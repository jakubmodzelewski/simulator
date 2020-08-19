import {Node} from "./Node";

export class Link {
  id: string;
  interfaceA: Node;
  interfaceB: Node;

  constructor(nodeA : Node, nodeB : Node) {
    this.id = nodeA.id;
    this.interfaceA = nodeA;
    this.interfaceB = nodeB;
    console.log("Link between " + nodeA.name + " and " + nodeB.name + " has been created");
  }
}
