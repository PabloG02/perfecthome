package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.Client;
import com.pablog.perfecthome.repository.ClientRepository;
import com.pablog.perfecthome.specification.ClientSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Optional<Client> findById(String username) {
        return clientRepository.findById(username);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    // Soft delete -> The user has to be kept in the database, to keep the history of the transactions
    public void deleteById(String username) {
        clientRepository.findById(username).ifPresent(client -> {
            client.setIsActive(false);
            clientRepository.save(client);
        });
    }

    public Page<Client> findAll(Specification<Client> spec, Pageable pageable) {
        return clientRepository.findAll(spec, pageable);
    }

    public Specification<Client> buildSpecification(ClientSpecificationBuilder builder) {
        return builder.build();
    }

}
