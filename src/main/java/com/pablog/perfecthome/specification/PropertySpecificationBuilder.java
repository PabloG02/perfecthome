package com.pablog.perfecthome.specification;

import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.entity.Property_;
import com.pablog.perfecthome.enums.PropertyType;
import org.springframework.data.jpa.domain.Specification;

import java.time.Year;

public class PropertySpecificationBuilder {

    private Specification<Property> specification;

    public PropertySpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public PropertySpecificationBuilder withAddress(String address) {
        if (address != null) {
            specification = specification.and((root, query, builder) ->
                    builder.like(root.get(Property_.address), "%" + address + "%"));
        }
        return this;
    }

    public PropertySpecificationBuilder withSize(Double size) {
        if (size != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.size), size));
        }
        return this;
    }

    public PropertySpecificationBuilder withRooms(Integer rooms) {
        if (rooms != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.rooms), rooms));
        }
        return this;
    }

    public PropertySpecificationBuilder withBathrooms(Integer bathrooms) {
        if (bathrooms != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.bathrooms), bathrooms));
        }
        return this;
    }

    public PropertySpecificationBuilder withGarage(Boolean hasGarage) {
        if (hasGarage != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.hasGarage), hasGarage));
        }
        return this;
    }

    public PropertySpecificationBuilder withElevator(Boolean hasElevator) {
        if (hasElevator != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.hasElevator), hasElevator));
        }
        return this;
    }

    public PropertySpecificationBuilder withConstructionYear(Year constructionYear) {
        if (constructionYear != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.constructionYear), constructionYear));
        }
        return this;
    }

    public PropertySpecificationBuilder withPropertyType(PropertyType propertyType) {
        if (propertyType != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get(Property_.propertyType), propertyType));
        }
        return this;
    }

    public Specification<Property> build() {
        return specification;
    }
}
