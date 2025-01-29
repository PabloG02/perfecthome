package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.Transaction;
import com.pablog.perfecthome.entity.Transaction_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecificationBuilder {

    private Specification<Transaction> specification;

    public TransactionSpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public TransactionSpecificationBuilder withDate(LocalDateTime date) {
        if (date != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Transaction_.date), date));
        }
        return this;
    }

    public TransactionSpecificationBuilder withClientId(String clientId) {
        if (clientId != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Transaction_.client).get("username"), clientId));
        }
        return this;
    }

    public TransactionSpecificationBuilder withListingId(Long listingId) {
        if (listingId != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Transaction_.listing).get("id"), listingId));
        }
        return this;
    }

    public Specification<Transaction> build() {
        return specification;
    }
}