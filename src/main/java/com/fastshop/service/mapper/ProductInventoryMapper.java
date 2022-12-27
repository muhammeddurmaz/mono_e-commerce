package com.fastshop.service.mapper;

import com.fastshop.domain.Product;
import com.fastshop.domain.ProductInventory;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.ProductInventoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductInventory} and its DTO {@link ProductInventoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductInventoryMapper extends EntityMapper<ProductInventoryDTO, ProductInventory> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ProductInventoryDTO toDto(ProductInventory s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
