package com.fastshop.service.mapper;

import com.fastshop.domain.Category;
import com.fastshop.domain.ProductType;
import com.fastshop.service.dto.CategoryDTO;
import com.fastshop.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "type", source = "type", qualifiedByName = "productTypeId")
    CategoryDTO toDto(Category s);

    @Named("productTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductTypeDTO toDtoProductTypeId(ProductType productType);
}
