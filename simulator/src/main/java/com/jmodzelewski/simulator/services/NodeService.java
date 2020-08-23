package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.dto.NodeDTO;
import com.jmodzelewski.simulator.database.NodeRepository;
import com.jmodzelewski.simulator.model.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.google.common.net.InetAddresses;

@Service
@AllArgsConstructor
@Slf4j
public class NodeService {
    private final NodeRepository nodeRepository;

    @Transactional
    public NodeDTO save(NodeDTO nodeDTO) {
        Node node = nodeRepository.save(mapDTOtoNode(nodeDTO));
        nodeDTO.setId(node.getId());
        switch (node.getType()) {
            case CLIENT:
                nodeDTO.setName("Client " + node.getId());
                break;
            case ROUTER:
                nodeDTO.setName("Router " + node.getId());
                break;
            default:
                nodeDTO.setName("Node " + node.getId());
                break;
        }

        //Losowy adres IP
        Random r = new Random();
        nodeDTO.setLoopback(InetAddresses.fromInteger(r.nextInt()).getHostAddress());
        return nodeDTO;
    }

    @Transactional
    public List<NodeDTO> getAll() {
        return nodeRepository.findAll()
                .stream()
                .map(this::mapNodeToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NodeDTO getNode(Long id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Node with id:" + id + " not found."));
        return mapNodeToDTO(node);
    }

    @Transactional
    public List<NodeDTO> deleteAll() {
        nodeRepository.deleteAll();
        return new LinkedList<>();
    }

    @Transactional
    public List<NodeDTO> deleteById(Long id) {
        nodeRepository.deleteById(id);
        return getAll();
    }

    public Node mapDTOtoNode(NodeDTO nodeDTO) {
        Node node;
        if (nodeDTO.getId() != null) {
            node =  nodeRepository.findById(nodeDTO.getId()).orElseThrow(
                    () -> new RuntimeException("Error: Node with id:" + nodeDTO.getId() + " not found.")
            );
        } else {
            node = new Node();
            node.setId(nodeDTO.getId());
        }
        node.setName(nodeDTO.getName());
        node.setLoopback(nodeDTO.getLoopback());
        node.setType(nodeDTO.getType());

        node.setInterfaces(nodeDTO.getInterfaces());
        node.setRoutingTable(nodeDTO.getRoutingTable());

        node.setActualX(nodeDTO.getActualX());
        node.setActualY(nodeDTO.getActualY());
        node.setPreviousX(nodeDTO.getPreviousX());
        node.setPreviousY(nodeDTO.getPreviousY());

        return node;
    }

    public NodeDTO mapNodeToDTO(Node node) {
        NodeDTO nodeDTO = new NodeDTO();

        nodeDTO.setId(node.getId());
        nodeDTO.setName(node.getName());
        nodeDTO.setLoopback(node.getLoopback());
        nodeDTO.setType(node.getType());

        nodeDTO.setInterfaces(node.getInterfaces());
        nodeDTO.setRoutingTable(node.getRoutingTable());

        nodeDTO.setActualX(node.getActualX());
        nodeDTO.setActualY(node.getActualY());
        nodeDTO.setPreviousX(node.getPreviousX());
        nodeDTO.setPreviousY(node.getPreviousY());

        return nodeDTO;
    }

    private String generateIPAddress(int p1, int p2, int p3) {

        StringBuilder sb = null;

        int b1 = (p1 >> 24) & 0xff;
        int b2 = (p2 >> 16) & 0xff;
        int b3 = (p3 >>  8) & 0xff;
        int b4 = 0;

        String ip1 = Integer.toString(b1);
        String ip2 = Integer.toString(b2);
        String ip3 = Integer.toString(b3);
        String ip4 = Integer.toString(b4);

        //Now the IP is b1.b2.b3.b4
        sb = new StringBuilder();
        sb.append(ip1).append(".").append(ip2).append(".").append(ip3).append(".").append(ip4);
        // System.out.println(sb);

        return sb.toString();

    }
}
