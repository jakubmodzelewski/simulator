package com.jmodzelewski.simulator.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance
public abstract class Node {
    @Id
    private UUID id;

    int x;
    int y;

//    List<Interface> interfaces;
//    Interface loopback;

}
