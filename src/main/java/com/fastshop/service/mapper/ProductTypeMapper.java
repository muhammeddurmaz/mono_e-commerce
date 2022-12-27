package com.fastshop.service.mapper;

import com.fastshop.domain.ProductType;
import com.fastshop.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductType} and its DTO {@link ProductTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductTypeMapper extends EntityMapper<ProductTypeDTO, ProductType> {}
