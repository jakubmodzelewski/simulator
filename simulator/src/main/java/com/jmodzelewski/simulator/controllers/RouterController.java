package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.controllers.dto.RouterDTO;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.services.RouterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/main/router")
@Slf4j
@AllArgsConstructor
public class RouterController {
    private final RouterService routerService;
    private final RouterRepository routerRepository;

    @GetMapping("/all")
    public ResponseEntity<List<RouterDTO>> all() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(routerService.getAll());
    }

    @PostMapping
    public ResponseEntity<RouterDTO> add(@RequestBody RouterDTO routerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routerService.save(routerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouterDTO> getRouter(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(routerService.getRouter(id));
    }

    @DeleteMapping
    public void deleteAll() {
        this.routerRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.routerRepository.deleteById(id);
    }

}
