package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    private Node nodeA;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    private Node nodeB;

    private String interfaceA;
    private String interfaceB;

    int xA;
    int yA;
    int xB;
    int yB;
}
