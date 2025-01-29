package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.entity.RealStateAgency_;
import org.springframework.data.jpa.domain.Specification;

public class RealStateAgencySpecificationBuilder {

    private Specification<RealStateAgency> specification;

    public RealStateAgencySpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public RealStateAgencySpecificationBuilder withName(String name) {
        if (name != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(RealStateAgency_.name), "%" + name + "%"));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withDescription(String description) {
        if (description != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(RealStateAgency_.description), "%" + description + "%"));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withAddress(String address) {
        if (address != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(RealStateAgency_.address), "%" + address + "%"));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withPhone(String phone) {
        if (phone != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(RealStateAgency_.phone), "%" + phone + "%"));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withEmail(String email) {
        if (email != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(RealStateAgency_.email), "%" + email + "%"));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withWebsite(String website) {
        if (website != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(RealStateAgency_.website), "%" + website + "%"));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withCif(String cif) {
        if (cif != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(RealStateAgency_.cif), cif));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withIsVerified(Boolean isVerified) {
        if (isVerified != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(RealStateAgency_.isVerified), isVerified));
        }
        return this;
    }

    public RealStateAgencySpecificationBuilder withIsDisabled(Boolean isDisabled) {
        specification = specification.and((root, query, builder) ->
                builder.equal(root.get(RealStateAgency_.isDisabled), isDisabled != null && isDisabled));
        return this;
    }

    public Specification<RealStateAgency> build() {
        return specification;
    }
}