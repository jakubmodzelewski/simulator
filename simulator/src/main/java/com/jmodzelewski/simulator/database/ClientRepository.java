package com.jmodzelewski.simulator.database;

import com.jmodzelewski.simulator.model.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends NodeRepository<Client> {
}
