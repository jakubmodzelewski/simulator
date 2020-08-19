package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String name;
    NodeType type;

    @ElementCollection
    @CollectionTable(name = "interfaces")
    List<String> interfaces;

    int actualX;
    int actualY;
    int previousX;
    int previousY;
}
