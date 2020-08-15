package com.jmodzelewski.simulator.services;

import com.jmodzelewski.simulator.dto.ClientDTO;
import com.jmodzelewski.simulator.database.ClientRepository;
import com.jmodzelewski.simulator.model.Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
            client =  clientRepository.findById(clientDTO.getId()).orElseThrow(
                    () -> new RuntimeException("Error: Router with id:" + clientDTO.getId() + " not found.")
            );
            client.setX(clientDTO.getX());
            client.setY(clientDTO.getY());

            return client;
        }
        else {
            client = new Client();
            client.setId(clientDTO.getId());
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
        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setId(client.getId());
        clientDTO.setX(client.getX());
        clientDTO.setY(client.getY());

        return clientDTO;
    }

    public ClientDTO getClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Client with id:" + id + " not found."));
        return mapToDTO(client);
    }
}
