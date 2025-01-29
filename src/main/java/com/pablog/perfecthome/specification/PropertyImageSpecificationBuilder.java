package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.PropertyImage;
import com.pablog.perfecthome.entity.PropertyImage_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PropertyImageSpecificationBuilder {

    private Specification<PropertyImage> specification;

    public PropertyImageSpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public PropertyImageSpecificationBuilder withUrl(String url) {
        if (url != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(PropertyImage_.url), "%" + url + "%"));
        }
        return this;
    }

    public PropertyImageSpecificationBuilder withDescription(String description) {
        if (description != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(PropertyImage_.description), "%" + description + "%"));
        }
        return this;
    }

    public PropertyImageSpecificationBuilder withUploadDate(LocalDateTime uploadDate) {
        if (uploadDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(PropertyImage_.uploadDate), uploadDate));
        }
        return this;
    }

    public PropertyImageSpecificationBuilder withPropertyId(Long propertyId) {
        if (propertyId != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(PropertyImage_.property).get("id"), propertyId)); // Assuming Property has an 'id' field
        }
        return this;
    }


    public Specification<PropertyImage> build() {
        return specification;
    }
}
