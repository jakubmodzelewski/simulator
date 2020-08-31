package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Simulation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @ManyToMany
    List<Node> nodes = new LinkedList<>();

    @ManyToMany
    List<Link> links = new LinkedList<>();
}
