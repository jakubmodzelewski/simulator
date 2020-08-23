package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    String loopback;

    NodeType type;

    @ElementCollection
    @CollectionTable(name = "interfaces")
    List<String> interfaces;

    @ElementCollection
    @CollectionTable(name = "routingTable")
    Map<String, String> routingTable;

    int actualX;
    int actualY;
    int previousX;
    int previousY;
}
