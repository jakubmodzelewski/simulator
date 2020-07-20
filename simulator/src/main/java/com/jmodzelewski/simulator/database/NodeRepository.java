package com.jmodzelewski.simulator.database;

import com.jmodzelewski.simulator.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface NodeRepository<T extends Node> extends JpaRepository<T, UUID> {
}
