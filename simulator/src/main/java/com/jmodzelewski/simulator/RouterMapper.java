package com.jmodzelewski.simulator;

import com.jmodzelewski.simulator.controllers.viewModel.RouterViewModel;
import com.jmodzelewski.simulator.controllers.viewModel.NodeViewModel;
import com.jmodzelewski.simulator.controllers.viewModel.RouterViewModel;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.model.Node;
import com.jmodzelewski.simulator.model.Router;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RouterMapper {
    private RouterRepository routerRepository;

    public RouterMapper(RouterRepository routerRepository) {
        this.routerRepository = routerRepository;
    }

    public Node convertRouterToEntity(RouterViewModel routerViewModel) {
        Node node;
        if (routerViewModel.getId() != null) {
            node =  routerRepository.findById(UUID.fromString(routerViewModel.getId())).get();
            node.setX(routerViewModel.getX());
            node.setY(routerViewModel.getY());

            return node;
        }
        else {
            node = new Router();
            node.setId(UUID.randomUUID());
            node.setX(routerViewModel.getX());
            node.setY(routerViewModel.getY());

            return node;
        }
    }


}
