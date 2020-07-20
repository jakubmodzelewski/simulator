package com.jmodzelewski.simulator;

import com.jmodzelewski.simulator.controllers.viewModel.ClientViewModel;
import com.jmodzelewski.simulator.database.ClientRepository;
import com.jmodzelewski.simulator.model.Client;
import com.jmodzelewski.simulator.model.Node;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientMapper {
    private ClientRepository clientRepository;

    public ClientMapper(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Node convertClientToEntity(ClientViewModel clientViewModel) {
        Node node;
        if (clientViewModel.getId() != null) {
            node =  clientRepository.findById(UUID.fromString(clientViewModel.getId())).get();
            node.setX(clientViewModel.getX());
            node.setY(clientViewModel.getY());

            return node;
        }
        else {
            node = new Client();
            node.setId(UUID.randomUUID());
            node.setX(clientViewModel.getX());
            node.setY(clientViewModel.getY());

            return node;
        }
    }


}
