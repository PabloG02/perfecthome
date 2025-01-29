package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.dto.TransactionDTO;
import com.pablog.perfecthome.entity.Client;
import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Transaction;
import com.pablog.perfecthome.service.ClientService;
import com.pablog.perfecthome.service.ListingService;
import com.pablog.perfecthome.service.TransactionService;
import com.pablog.perfecthome.specification.TransactionSpecificationBuilder;
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
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class TransactionController {

    private final TransactionService transactionService;
    private final ClientService clientService;
    private final ListingService listingService;

    @Autowired
    public TransactionController(TransactionService transactionService, ClientService clientService, ListingService listingService) {
        this.transactionService = transactionService;
        this.clientService = clientService;
        this.listingService = listingService;
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAllTransactions(
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) Long listingId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        TransactionSpecificationBuilder builder = new TransactionSpecificationBuilder()
                .withDate(date)
                .withClientId(clientId)
                .withListingId(listingId);

        Page<Transaction> transactions = transactionService.findAll(transactionService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);
        return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        Client client = clientService.findById(transactionDTO.getUsername()).orElseThrow();
        Listing listing = listingService.findById(transactionDTO.getListingId()).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setListing(listing);

        Transaction createdTransaction = transactionService.save(transaction);
        URI location = buildLocation(createdTransaction.getId());
        return ResponseEntity.created(location).body(createdTransaction);
    }

    // PUT is not suitable for Transaction as it's audit-like and shouldn't be updated.

    // DELETE is not suitable for Transaction. Once a transaction is done, a property cannot be un-sold (returned).

    private URI buildLocation(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}

