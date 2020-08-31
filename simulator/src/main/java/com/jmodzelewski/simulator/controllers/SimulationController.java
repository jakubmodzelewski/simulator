package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.dto.NodeDTO;
import com.jmodzelewski.simulator.dto.SimulationDTO;
import com.jmodzelewski.simulator.services.SimulationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/workspace/simulation")
@Slf4j
@AllArgsConstructor
public class SimulationController {
    private final SimulationService simulationService;

    @PostMapping
    public ResponseEntity<SimulationDTO> save(@RequestBody SimulationDTO simulationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulationService.save(simulationDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulationDTO> load(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(simulationService.load(id));
    }

}
