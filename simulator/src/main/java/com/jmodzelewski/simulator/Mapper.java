package com.jmodzelewski.simulator;

import com.jmodzelewski.simulator.controllers.viewModel.RouterViewModel;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.model.Router;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Mapper {
    private RouterRepository routerRepository;

    public Router convertToEntity(RouterViewModel routerViewModel) {
        if (routerViewModel.getId() != null) {
            return new Router(UUID.fromString(routerViewModel.getId()));
        }
        else {
            return new Router(UUID.randomUUID());
        }
    }


}
