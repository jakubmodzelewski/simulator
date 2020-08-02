package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance
@Builder
public class Node {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    int x;
    int y;

//    List<Interface> interfaces;
//    Interface loopback;

}
