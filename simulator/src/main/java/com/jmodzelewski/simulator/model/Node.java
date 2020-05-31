package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public abstract class Node {
    @Id
    private UUID id;

    int x;
    int y;

//    List<Interface> interfaces;
//    Interface loopback;

}
