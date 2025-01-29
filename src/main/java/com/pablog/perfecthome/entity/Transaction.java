package com.pablog.perfecthome.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transaction {
//    @EmbeddedId
//    private TransactionId id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Changed to regular ID generation
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Client cannot be null")
    private Client client;

    @ManyToOne
    @JoinColumn(nullable = false, unique = true)
    @NotNull(message = "Listing cannot be null")
    private Listing listing;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    public Transaction() {
    }

//    public Transaction(TransactionId id, LocalDateTime date) {
//        this.id = id;
//        this.date = date;
//    }
//
//    public TransactionId getId() {
//        return id;
//    }
//
//    public void setId(TransactionId id) {
//        this.id = id;
//    }

    public Transaction(Client client, Listing listing) {
        this.client = client;
        this.listing = listing;
    }

    public Transaction(Long id, Client client, Listing listing, LocalDateTime date) {
        this.id = id;
        this.client = client;
        this.listing = listing;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(client, that.client) && Objects.equals(listing, that.listing) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, listing, date);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", client=" + client +
                ", listing=" + listing +
                ", date=" + date +
                '}';
    }
}
