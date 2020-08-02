package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.controllers.dto.ClientDTO;
import com.jmodzelewski.simulator.database.ClientRepository;
import com.jmodzelewski.simulator.services.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/main/client")
@AllArgsConstructor
@Slf4j
public class ClientController {
    private final ClientRepository clientRepository;
    private final ClientService clientService;


    @GetMapping("/all")
    public ResponseEntity<List<ClientDTO>> all() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientService.getAll());
    }

    @PostMapping
    public ResponseEntity<ClientDTO> add(@RequestBody ClientDTO clientDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.save(clientDTO));
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
