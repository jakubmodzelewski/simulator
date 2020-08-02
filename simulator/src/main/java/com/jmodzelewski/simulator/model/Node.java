package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance
@Builder
public class Node {
    @Id
    private Long id;

    int x;
    int y;

//    List<Interface> interfaces;
//    Interface loopback;

}
