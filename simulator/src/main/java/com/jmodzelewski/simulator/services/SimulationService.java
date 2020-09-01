package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.database.SimulationRepository;
import com.jmodzelewski.simulator.dto.LinkDTO;
import com.jmodzelewski.simulator.dto.NodeDTO;
import com.jmodzelewski.simulator.dto.SimulationDTO;
import com.jmodzelewski.simulator.model.Link;
import com.jmodzelewski.simulator.model.Node;
import com.jmodzelewski.simulator.model.Simulation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;

@Service
@AllArgsConstructor
@Slf4j
public class SimulationService {
    private final SimulationRepository simulationRepository;
    private final NodeService nodeService;
    private final LinkService linkService;

    @Transactional
    public SimulationDTO save(SimulationDTO simulationDTO) {
        Simulation simulation = simulationRepository.save(mapDTOtoSimulation(simulationDTO));
        simulationDTO.setId(simulation.getId());

        return simulationDTO;
    }

    @Transactional
    public SimulationDTO load(Long id) {
        Simulation simulation = simulationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Simulation with id:" + id + " not found."));
        return mapSimulationToDTO(simulation);
    }

    public Simulation mapDTOtoSimulation(SimulationDTO simulationDTO) {
        Simulation simulation;
        if (simulationDTO.getId() != null) {
            simulation =  simulationRepository.findById(simulationDTO.getId()).orElseThrow(
                    () -> new RuntimeException("Error: Simulation with id:" + simulationDTO.getId() + " not found.")
            );

            simulation.setNodes(new LinkedList<>());
            simulation.setLinks(new LinkedList<>());
            for (NodeDTO nodeDTO : simulationDTO.getNodes()) {
                simulation.getNodes().add(nodeService.mapDTOtoNode(nodeDTO));
            }
            for (LinkDTO linkDTO : simulationDTO.getLinks()) {
                simulation.getLinks().add(linkService.mapLinkDTO(linkDTO));
            }
        } else {
            simulation = new Simulation();
            simulation.setId(simulationDTO.getId());

            for (NodeDTO nodeDTO : simulationDTO.getNodes()) {
                simulation.getNodes().add(nodeService.mapDTOtoNode(nodeDTO));
            }
            for (LinkDTO linkDTO : simulationDTO.getLinks()) {
                simulation.getLinks().add(linkService.mapLinkDTO(linkDTO));
            }
        }

        return simulation;
    }

    public SimulationDTO mapSimulationToDTO(Simulation simulation) {
        SimulationDTO simulationDTO = new SimulationDTO();

        simulationDTO.setId(simulation.getId());
        for (Node node : simulation.getNodes()) {
            simulationDTO.getNodes().add(nodeService.mapNodeToDTO(node));
        }
        for (Link link : simulation.getLinks()) {
            simulationDTO.getLinks().add(linkService.mapToDTO(link));
        }

        return simulationDTO;
    }

}
