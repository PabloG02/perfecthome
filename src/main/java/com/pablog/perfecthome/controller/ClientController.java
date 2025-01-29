package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.entity.Client;
import com.pablog.perfecthome.service.ClientService;
import com.pablog.perfecthome.specification.ClientSpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Page<Client>> getAllClients(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(sort = "username", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        ClientSpecificationBuilder builder = new ClientSpecificationBuilder()
                .withUsername(username)
                .withEmail(email)
                .withPhone(phone)
                .withIsActive(isActive);

        Page<Client> clients = clientService.findAll(clientService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Client> getClientById(@PathVariable String username) {
        Optional<Client> client = clientService.findById(username);
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        Client createdClient = clientService.save(client);
        URI location = buildLocation(createdClient.getUsername());
        return ResponseEntity.created(location).body(createdClient);
    }

    @PutMapping("/{username}")
    public ResponseEntity<Client> updateClient(@PathVariable String username, @Valid @RequestBody Client clientDetails) {
        Optional<Client> existingClient = clientService.findById(username);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            // Update fields (partial updates)
            client.setPassword(clientDetails.getPassword()); // TODO: It should not be done like this in the real world
            client.setEmail(clientDetails.getEmail());
            client.setPhone(clientDetails.getPhone());
            client.setIsActive(clientDetails.getIsActive());
            Client updatedClient = clientService.save(client);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable String username) {
        if (clientService.findById(username).isPresent()) {
            clientService.deleteById(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private URI buildLocation(String username) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(username)
                .toUri();
    }
}
