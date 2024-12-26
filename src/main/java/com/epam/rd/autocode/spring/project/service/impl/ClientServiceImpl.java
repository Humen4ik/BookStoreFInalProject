package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private ModelMapper mapper;
    private ClientRepository clientRepository;

    public ClientServiceImpl(ModelMapper mapper, ClientRepository clientRepository) {
        this.mapper = mapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(client -> mapper.map(client, ClientDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientByEmail(String email) {
        Client client = clientRepository.getClientByEmail(email).orElseThrow(() -> new NotFoundException("Client not found"));
        return mapper.map(client, ClientDTO.class);
    }

    @Override
    public ClientDTO updateClientByEmail(String email, ClientDTO clientDTO) {
        Client oldClient = clientRepository.getClientByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        oldClient.setName(clientDTO.getName());
        oldClient.setBalance(clientDTO.getBalance());

        if (!email.equals(clientDTO.getEmail())) {
            throw new IllegalArgumentException("Email cannot be updated");
        }

        if (clientDTO.getPassword() != null) {
            oldClient.setPassword(clientDTO.getPassword());
        }

        Client savedClient = clientRepository.save(oldClient);
        return mapper.map(savedClient, ClientDTO.class);

    }

    @Override
    public void deleteClientByEmail(String email) {
        clientRepository.deleteClientByEmail(email);
    }

    @Override
    public ClientDTO addClient(ClientDTO client) {
        Client newClient = mapper.map(client, Client.class);
        return mapper.map(clientRepository.save(newClient), ClientDTO.class);
    }

    public Client getAuthenticatedClient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return clientRepository.getClientByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Client not found"));
    }

}
