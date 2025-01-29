package com.pablog.perfecthome.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Embeddable
public class TransactionId {
    @ManyToOne
    private Client client;

    @OneToOne
    private Listing listing;

    public TransactionId() {
    }

    public TransactionId(Client client, Listing listing) {
        this.client = client;
        this.listing = listing;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionId that = (TransactionId) o;
        return Objects.equals(client, that.client) && Objects.equals(listing, that.listing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, listing);
    }

    @Override
    public String toString() {
        return "TransactionId{" +
                "client=" + client +
                ", listing=" + listing +
                '}';
    }
}
