package com.jmodzelewski.simulator.database;

import com.jmodzelewski.simulator.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository  extends JpaRepository<Simulation, Long> {
}
