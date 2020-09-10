package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Column(name = "interface")
    List<String> interfaces;

    @ElementCollection
    @CollectionTable(name = "routing_table")
    @MapKeyColumn (name="network")
    @Column(name="next_hop")
    Map<String, String> routingTable = new HashMap<>();

    int actualX;
    int actualY;
    int previousX;
    int previousY;
}

