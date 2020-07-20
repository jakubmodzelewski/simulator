package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.ClientMapper;
import com.jmodzelewski.simulator.RouterMapper;
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
    private RouterMapper routerMapper;

    public RouterController(RouterRepository routerRepository, RouterMapper routerMapper) {
        this.routerRepository = routerRepository;
        this.routerMapper = routerMapper;
    }

    @GetMapping("/all")
    public List<Router> all() {
        return routerRepository.findAll();
    }

    @PostMapping
    public Router add(@RequestBody RouterViewModel nodeViewModel,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        Router router = (Router) routerMapper.convertRouterToEntity(nodeViewModel);

        //Zapis do bazy danych
        this.routerRepository.save(router);

        return router;
    }

    @DeleteMapping
    public void deleteAll() {
        this.routerRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.routerRepository.deleteById(UUID.fromString(id));
    }

}
