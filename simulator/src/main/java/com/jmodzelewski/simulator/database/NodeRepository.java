package com.jmodzelewski.simulator.database;

import com.jmodzelewski.simulator.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {
}
