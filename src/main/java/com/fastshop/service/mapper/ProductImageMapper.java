package com.fastshop.service.mapper;

import com.fastshop.domain.Product;
import com.fastshop.domain.ProductImage;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.ProductImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductImage} and its DTO {@link ProductImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductImageMapper extends EntityMapper<ProductImageDTO, ProductImage> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ProductImageDTO toDto(ProductImage s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
