package com.jmodzelewski.simulator;

import com.jmodzelewski.simulator.controllers.viewModel.NodeViewModel;
import com.jmodzelewski.simulator.database.NodeRepository;
import com.jmodzelewski.simulator.model.Node;
import com.jmodzelewski.simulator.model.Router;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Mapper {
    private NodeRepository nodeRepository;

    public Mapper(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public Node convertToEntity(NodeViewModel nodeViewModel) {
        Node node;
        if (nodeViewModel.getId() != null) {
            node =  nodeRepository.findById(UUID.fromString(nodeViewModel.getId())).get();
            node.setX(nodeViewModel.getX());
            node.setY(nodeViewModel.getY());

            return node;
        }
        else {
            node = new Router();
            node.setId(UUID.randomUUID());
            node.setX(nodeViewModel.getX());
            node.setY(nodeViewModel.getY());

            return node;
        }
    }


}
