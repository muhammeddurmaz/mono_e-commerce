package com.fastshop.service.mapper;

import com.fastshop.domain.Category;
import com.fastshop.domain.SubCategory;
import com.fastshop.service.dto.CategoryDTO;
import com.fastshop.service.dto.SubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubCategory} and its DTO {@link SubCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubCategoryMapper extends EntityMapper<SubCategoryDTO, SubCategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    SubCategoryDTO toDto(SubCategory s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
