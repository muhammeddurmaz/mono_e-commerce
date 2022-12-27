package com.fastshop.service.mapper;

import com.fastshop.domain.Product;
import com.fastshop.domain.Property;
import com.fastshop.domain.PropertyDes;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.PropertyDTO;
import com.fastshop.service.dto.PropertyDesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PropertyDes} and its DTO {@link PropertyDesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PropertyDesMapper extends EntityMapper<PropertyDesDTO, PropertyDes> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    PropertyDesDTO toDto(PropertyDes s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
