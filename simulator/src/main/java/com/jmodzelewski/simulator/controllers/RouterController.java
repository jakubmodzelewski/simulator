package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.Mapper;
import com.jmodzelewski.simulator.controllers.viewModel.RouterViewModel;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.model.Router;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/main/router")
public class RouterController {
    private RouterRepository routerRepository;
    private Mapper mapper;

    public RouterController(RouterRepository routerRepository, Mapper mapper) {
        this.routerRepository = routerRepository;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<Router> all() {
        return this.routerRepository.findAll();
    }

    @PostMapping
    public Router add(@RequestBody RouterViewModel routerViewModel,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        Router router = mapper.convertToEntity(routerViewModel);

        //Zapis do bazy danych
        this.routerRepository.save(router);

        return router;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
    }
}
