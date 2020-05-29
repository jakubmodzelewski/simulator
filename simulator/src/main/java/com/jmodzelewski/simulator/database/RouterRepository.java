package com.jmodzelewski.simulator.database;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jmodzelewski.simulator.model.Router;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouterRepository extends JpaRepository<Router, UUID> {}
