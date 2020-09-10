package com.jmodzelewski.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    private String username;
    private String password;

    @Email
    private String email;

    @ElementCollection
    @Column(name = "simulation")
    List<Simulation> simulations;

    private Long lastUsedSimulationId;

    private Instant created;

}
