package com.fastshop.service.mapper;

import com.fastshop.domain.Category;
import com.fastshop.domain.Property;
import com.fastshop.service.dto.CategoryDTO;
import com.fastshop.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Property} and its DTO {@link PropertyDTO}.
 */
@Mapper(componentModel = "spring")
public interface PropertyMapper extends EntityMapper<PropertyDTO, Property> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    PropertyDTO toDto(Property s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
