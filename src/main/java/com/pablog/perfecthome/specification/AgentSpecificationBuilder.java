package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.Agent;
import com.pablog.perfecthome.entity.Agent_;
import org.springframework.data.jpa.domain.Specification;

public class AgentSpecificationBuilder {

    private Specification<Agent> specification;

    public AgentSpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public AgentSpecificationBuilder withUsername(String username) {
        if (username != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Agent_.username), "%" + username + "%"));
        }
        return this;
    }

    public AgentSpecificationBuilder withEmail(String email) {
        if (email != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Agent_.email), "%" + email + "%"));
        }
        return this;
    }

    public AgentSpecificationBuilder withPhone(String phone) {
        if (phone != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Agent_.phone), "%" + phone + "%"));
        }
        return this;
    }

    public AgentSpecificationBuilder withIsActive(Boolean isActive) {
        if (isActive != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Agent_.isActive), isActive));
        }
        return this;
    }

    public AgentSpecificationBuilder withEmployeeNumber(Long employeeNumber) {
        if (employeeNumber != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Agent_.employeeNumber), employeeNumber));
        }
        return this;
    }

    public Specification<Agent> build() {
        return specification;
    }
}
