package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Listing_;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ListingSpecificationBuilder {

    private Specification<Listing> specification;

    public ListingSpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public ListingSpecificationBuilder withTitle(String title) {
        if (title != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Listing_.title), "%" + title + "%"));
        }
        return this;
    }

    public ListingSpecificationBuilder withDescription(String description) {
        if (description != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Listing_.description), "%" + description + "%"));
        }
        return this;
    }

    public ListingSpecificationBuilder withPrice(BigDecimal price) {
        if (price != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Listing_.price), price));
        }
        return this;
    }

    public ListingSpecificationBuilder withPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null) {
            specification = specification.and((root, query, builder) ->
                    builder.between(root.get(Listing_.price), minPrice, maxPrice));
        } else if (minPrice != null) {
            specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get(Listing_.price), minPrice));
        } else if (maxPrice != null) {
            specification = specification.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get(Listing_.price), maxPrice));
        }
        return this;
    }

    public ListingSpecificationBuilder withPublicationDate(LocalDateTime publicationDate) {
        if (publicationDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Listing_.publicationDate), publicationDate));
        }
        return this;
    }

    public ListingSpecificationBuilder withPublicationDateAfter(LocalDateTime publicationDate) {
        if (publicationDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get(Listing_.publicationDate), publicationDate));
        }
        return this;
    }

    public ListingSpecificationBuilder withExpirationDate(LocalDateTime expirationDate) {
        if (expirationDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Listing_.expirationDate), expirationDate));
        }
        return this;
    }

//    public ListingSpecificationBuilder withPublisherId(Long publisherId) {
//        if (publisherId != null) {
//            specification = specification.and((root, query, builder) ->
//                    builder.equal(root.get(Listing_.publisher).get("id"), publisherId));
//        }
//        return this;
//    }
//
//    public ListingSpecificationBuilder withPropertyId(Long propertyId) {
//        if (propertyId != null) {
//            specification = specification.and((root, query, builder) ->
//                    builder.equal(root.get(Listing_.property).get("id"), propertyId));
//        }
//        return this;
//    }

    public Specification<Listing> build() {
        return specification;
    }
}
