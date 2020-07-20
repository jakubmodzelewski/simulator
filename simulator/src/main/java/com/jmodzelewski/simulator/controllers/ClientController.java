package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.ClientMapper;
import com.jmodzelewski.simulator.controllers.viewModel.ClientViewModel;
import com.jmodzelewski.simulator.database.ClientRepository;
import com.jmodzelewski.simulator.model.Client;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/main/client")
public class ClientController {
    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    public ClientController(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @GetMapping("/all")
    public List<Client> all() {
        return clientRepository.findAll();
    }

    @PostMapping
    public Client add(@RequestBody ClientViewModel nodeViewModel,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        Client client = (Client) clientMapper.convertClientToEntity(nodeViewModel);

        //Zapis do bazy danych
        this.clientRepository.save(client);

        return client;
    }

    @DeleteMapping
    public void deleteAll() {
        this.clientRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.clientRepository.deleteById(UUID.fromString(id));
    }

}
