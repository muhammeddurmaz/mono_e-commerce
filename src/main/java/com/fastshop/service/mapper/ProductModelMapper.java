package com.fastshop.service.mapper;

import com.fastshop.domain.ProductModel;
import com.fastshop.service.dto.ProductModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductModel} and its DTO {@link ProductModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductModelMapper extends EntityMapper<ProductModelDTO, ProductModel> {}
