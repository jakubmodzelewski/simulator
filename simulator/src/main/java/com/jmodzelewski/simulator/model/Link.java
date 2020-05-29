package com.jmodzelewski.simulator.model;

public class Link {
    private Interface interfaceA;
    private Interface interfaceB;

    public Link(String interfaceA, String interfaceB) {
        this.interfaceA = new Interface(interfaceA);
        this.interfaceB = new Interface(interfaceB);
    }
}
