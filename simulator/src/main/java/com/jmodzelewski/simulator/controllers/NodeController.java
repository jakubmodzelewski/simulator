package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.Mapper;
import com.jmodzelewski.simulator.controllers.viewModel.NodeViewModel;
import com.jmodzelewski.simulator.controllers.viewModel.RouterViewModel;
import com.jmodzelewski.simulator.database.NodeRepository;
import com.jmodzelewski.simulator.model.Node;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/main/node")
public class NodeController {
    private NodeRepository nodeRepository;
    private Mapper mapper;

    public NodeController(NodeRepository nodeRepository, Mapper mapper) {
        this.nodeRepository = nodeRepository;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<Node> all() {
        return nodeRepository.findAll();
    }

    @PostMapping
    public Node add(@RequestBody RouterViewModel routerViewModel,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        var node = mapper.convertToEntity(routerViewModel);

        //Zapis do bazy danych
        this.nodeRepository.save(node);

        return node;
    }

    @DeleteMapping
    public void deleteAll() {
        this.nodeRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.nodeRepository.deleteById(UUID.fromString(id));
    }

}
