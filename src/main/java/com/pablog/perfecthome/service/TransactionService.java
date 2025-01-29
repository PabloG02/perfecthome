package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.Transaction;
import com.pablog.perfecthome.repository.TransactionRepository;
import com.pablog.perfecthome.specification.TransactionSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Page<Transaction> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        // The transaction can only be done if the listing is active
        if (transaction.getListing().getPublicationDate().isAfter(LocalDateTime.now())
                || (transaction.getListing().getExpirationDate() != null && transaction.getListing().getExpirationDate().isBefore(LocalDateTime.now()))) {
            throw new IllegalStateException("The listing is no longer active");
        }

        // The client must have an active account
        if (!transaction.getClient().getIsActive()) {
            throw new IllegalStateException("The client account is no longer active");
        }

        transaction.getListing().setExpirationDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

//    public void deleteById(Long id) {
//        transactionRepository.deleteById(id);
//    }

    public Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable) {
        return transactionRepository.findAll(spec, pageable);
    }

    public Specification<Transaction> buildSpecification(TransactionSpecificationBuilder builder) {
        return builder.build();
    }
}
