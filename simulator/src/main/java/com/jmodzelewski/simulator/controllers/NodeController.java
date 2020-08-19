package com.jmodzelewski.simulator.controllers;

import com.jmodzelewski.simulator.dto.LinkDTO;
import com.jmodzelewski.simulator.dto.NodeDTO;
import com.jmodzelewski.simulator.services.LinkService;
import com.jmodzelewski.simulator.services.NodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/workspace/node")
@Slf4j
@AllArgsConstructor
public class NodeController {
    private final NodeService nodeService;
    private final LinkService linkService;

    @GetMapping("/all")
    public ResponseEntity<List<NodeDTO>> all() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nodeService.getAll());
    }

    @PostMapping
    public ResponseEntity<NodeDTO> add(@RequestBody NodeDTO nodeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nodeService.save(nodeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeDTO> getNode(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nodeService.getNode(id));
    }

    @DeleteMapping("/all")
    public ResponseEntity<List<NodeDTO>> deleteAll() {
        linkService.deleteAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nodeService.deleteAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<NodeDTO>> delete(@PathVariable Long id) {
        NodeDTO nodeDTO = nodeService.getNode(id);
        for (LinkDTO linkDTO : linkService.getAll()) {
            if (linkDTO.getInterfaceA().equals(nodeDTO) || linkDTO.getInterfaceB().equals(nodeDTO)) {
                linkService.deleteById(linkDTO.getId());
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(nodeService.deleteById(id));
    }
}
