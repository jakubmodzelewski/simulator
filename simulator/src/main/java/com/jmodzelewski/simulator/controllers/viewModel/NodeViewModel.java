package com.jmodzelewski.simulator.controllers.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NodeViewModel {
    String id = null;
    String name = null;

    int x = 0;
    int y = 0;
}
