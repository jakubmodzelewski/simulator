package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.database.LinkRepository;
import com.jmodzelewski.simulator.dto.LinkDTO;
import com.jmodzelewski.simulator.dto.NodeDTO;
import com.jmodzelewski.simulator.services.LinkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("workspace/link")
@Slf4j
@AllArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<LinkDTO> add(@RequestBody LinkDTO linkDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(linkService.save(linkDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LinkDTO>> all() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkDTO> getLink(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkService.getLink(id));
    }

    @DeleteMapping("/all")
    public ResponseEntity<List<LinkDTO>> deleteAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkService.deleteAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<LinkDTO>> delete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(linkService.deleteById(id));
    }

}
