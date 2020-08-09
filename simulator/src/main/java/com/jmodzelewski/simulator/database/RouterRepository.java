package com.jmodzelewski.simulator.database;

import com.jmodzelewski.simulator.model.Router;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouterRepository extends JpaRepository<Router, Long> {
}
