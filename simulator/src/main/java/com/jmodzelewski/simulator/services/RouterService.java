package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.dto.RouterDTO;
import com.jmodzelewski.simulator.database.RouterRepository;
import com.jmodzelewski.simulator.model.Router;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
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

    @Transactional
    public List<RouterDTO> getAll() {
        return routerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RouterDTO> deleteAll() {
        routerRepository.deleteAll();
        return new LinkedList<RouterDTO>();
    }

    public Router mapRouterDTO(RouterDTO routerDTO) {
        Router router;
        if (routerDTO.getId() != null) {
            router =  routerRepository.findById(routerDTO.getId()).orElseThrow(
                    () -> new RuntimeException("Error: Router with id:" + routerDTO.getId() + " not found.")
            );
            router.setActualX(routerDTO.getActualX());
            router.setActualY(routerDTO.getActualY());
            router.setPreviousX(routerDTO.getPreviousX());
            router.setPreviousY(routerDTO.getPreviousY());

            return router;
        }
        else {
            router = new Router();
            router.setId(routerDTO.getId());
            router.setActualX(routerDTO.getActualX());
            router.setActualY(routerDTO.getActualY());
            router.setPreviousX(routerDTO.getPreviousX());
            router.setPreviousY(routerDTO.getPreviousY());

            return router;
        }
    }

    private RouterDTO mapToDTO(Router router) {
        RouterDTO routerDTO = new RouterDTO();

        routerDTO.setId(router.getId());
        routerDTO.setActualX(router.getActualX());
        routerDTO.setActualY(router.getActualY());
        routerDTO.setPreviousX(router.getPreviousX());
        routerDTO.setPreviousY(router.getPreviousY());

        return routerDTO;
    }

    public RouterDTO getRouter(Long id) {
        Router router = routerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Router with id:" + id + " not found."));
        return mapToDTO(router);
    }

    public List<RouterDTO> deleteById(Long id) {
        routerRepository.deleteById(id);
        return getAll();
    }
}
