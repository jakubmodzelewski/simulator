package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.controllers.dto.ClientDTO;
import com.jmodzelewski.simulator.database.ClientRepository;
import com.jmodzelewski.simulator.model.Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public ClientDTO save(ClientDTO clientDTO) {
        Client client = clientRepository.save(mapClientDTO(clientDTO));
        clientDTO.setId(client.getId());
        return clientDTO;
    }

    public Client mapClientDTO(ClientDTO clientDTO) {
        Client client;
        if (clientDTO.getId() != null) {
            client =  clientRepository.findById(UUID.fromString(clientDTO.getId())).get();
            client.setX(clientDTO.getX());
            client.setY(clientDTO.getY());

            return client;
        }
        else {
            client = new Client();
            client.setId(UUID.randomUUID().toString());
            client.setX(clientDTO.getX());
            client.setY(clientDTO.getY());

            return client;
        }
    }

    @Transactional
    public List<ClientDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ClientDTO mapToDTO(Client client) {
        return (ClientDTO) ClientDTO.builder()
                .id(client.getId())
                .x(client.getX())
                .y(client.getY())
                .build();
    }
}
