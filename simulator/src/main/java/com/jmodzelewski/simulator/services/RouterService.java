package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.controllers.dto.RouterDTO;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.model.Router;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RouterService {
    private final RouterRepository routerRepository;

    @Transactional
    public RouterDTO save(RouterDTO routerDTO) {
        Router router = routerRepository.save(mapRouterDTO(routerDTO));
        routerDTO.setId(router.getId());
        return routerDTO;
    }

    public Router mapRouterDTO(RouterDTO routerDTO) {
        Router router;
        if (routerDTO.getId() != null) {
            router =  routerRepository.findById(routerDTO.getId()).orElseThrow(
                    () -> new RuntimeException("Error: Router with id:" + routerDTO.getId() + " not found.")
            );
            router.setX(routerDTO.getX());
            router.setY(routerDTO.getY());

            return router;
        }
        else {
            router = new Router();
            router.setId(1L);
            router.setX(routerDTO.getX());
            router.setY(routerDTO.getY());

            return router;
        }
    }

    @Transactional
    public List<RouterDTO> getAll() {
         return routerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RouterDTO mapToDTO(Router router) {
        return (RouterDTO) RouterDTO.builder()
                .id(router.getId())
                .x(router.getX())
                .y(router.getY())
                .build();
    }

    public RouterDTO getRouter(Long id) {
        Router router = routerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Router with id:" + id + " not found."));
        return mapToDTO(router);
    }
}
