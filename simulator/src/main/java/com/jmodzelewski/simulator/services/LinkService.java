package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.database.LinkRepository;
import com.jmodzelewski.simulator.dto.LinkDTO;
import com.jmodzelewski.simulator.model.Link;
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
public class LinkService {
    private final LinkRepository linkRepository;
    private final NodeService nodeService;

    @Transactional
    public LinkDTO save(LinkDTO linkDTO) {
        Link link = linkRepository.save(mapLinkDTO(linkDTO));
        linkDTO.setId(link.getId());
        linkDTO.setInterfaceA(nodeService.mapNodeToDTO(link.getInterfaceA()));
        linkDTO.setInterfaceB(nodeService.mapNodeToDTO(link.getInterfaceB()));
        return linkDTO;
    }

    @Transactional
    public List<LinkDTO> getAll() {
        return linkRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LinkDTO getLink(Long id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Link with id:" + id + " not found."));
        return mapToDTO(link);
    }

    @Transactional
    public List<LinkDTO> deleteAll() {
        linkRepository.deleteAll();
        return new LinkedList<LinkDTO>();
    }

    @Transactional
    public List<LinkDTO> deleteById(Long id) {
        linkRepository.deleteById(id);
        return getAll();
    }

    private Link mapLinkDTO(LinkDTO linkDTO) {
        Link link = new Link();
        link.setId(linkDTO.getId());
        link.setInterfaceA(nodeService.mapDTOtoNode(linkDTO.getInterfaceA()));
        link.setInterfaceB(nodeService.mapDTOtoNode(linkDTO.getInterfaceB()));
        return link;
    }

    private LinkDTO mapToDTO(Link link) {
        LinkDTO linkDTO = new LinkDTO();

        linkDTO.setId(link.getId());
        linkDTO.setInterfaceA(nodeService.mapNodeToDTO(link.getInterfaceA()));
        linkDTO.setInterfaceB(nodeService.mapNodeToDTO(link.getInterfaceB()));

        return linkDTO;
    }
}
