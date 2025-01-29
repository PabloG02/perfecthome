package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.Client;
import com.pablog.perfecthome.entity.Client_;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecificationBuilder {

    private Specification<Client> specification;

    public ClientSpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public ClientSpecificationBuilder withUsername(String username) {
        if (username != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Client_.username), "%" + username + "%"));
        }
        return this;
    }

    public ClientSpecificationBuilder withEmail(String email) {
        if (email != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Client_.email), "%" + email + "%"));
        }
        return this;
    }

    public ClientSpecificationBuilder withPhone(String phone) {
        if (phone != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Client_.phone), "%" + phone + "%"));
        }
        return this;
    }

    public ClientSpecificationBuilder withIsActive(Boolean isActive) {
        if (isActive != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Client_.isActive), isActive));
        }
        return this;
    }

    public Specification<Client> build() {
        return specification;
    }
}