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
        linkDTO.setNodeA(nodeService.mapNodeToDTO(link.getNodeA()));
        linkDTO.setNodeB(nodeService.mapNodeToDTO(link.getNodeB()));
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

    public Link mapLinkDTO(LinkDTO linkDTO) {
        Link link = new Link();
        link.setId(linkDTO.getId());
        link.setNodeA(nodeService.mapDTOtoNode(linkDTO.getNodeA()));
        link.setNodeB(nodeService.mapDTOtoNode(linkDTO.getNodeB()));

        link.setInterfaceA(linkDTO.getInterfaceA());
        link.setInterfaceB(linkDTO.getInterfaceB());

        link.setXA(linkDTO.getXA());
        link.setYA(linkDTO.getYA());
        link.setXB(linkDTO.getXB());
        link.setYB(linkDTO.getYB());

        return link;
    }

    public LinkDTO mapToDTO(Link link) {
        LinkDTO linkDTO = new LinkDTO();

        linkDTO.setId(link.getId());
        linkDTO.setNodeA(nodeService.mapNodeToDTO(link.getNodeA()));
        linkDTO.setNodeB(nodeService.mapNodeToDTO(link.getNodeB()));

        linkDTO.setInterfaceA(link.getInterfaceA());
        linkDTO.setInterfaceB(link.getInterfaceB());

        linkDTO.setXA(link.getXA());
        linkDTO.setYA(link.getYA());
        linkDTO.setXB(link.getXB());
        linkDTO.setYB(link.getYB());

        return linkDTO;
    }
}
