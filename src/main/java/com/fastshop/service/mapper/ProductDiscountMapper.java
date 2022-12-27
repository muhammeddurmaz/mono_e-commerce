package com.fastshop.service.mapper;

import com.fastshop.domain.Discount;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductDiscount;
import com.fastshop.service.dto.DiscountDTO;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.ProductDiscountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductDiscount} and its DTO {@link ProductDiscountDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductDiscountMapper extends EntityMapper<ProductDiscountDTO, ProductDiscount> {
    @Mapping(target = "discount", source = "discount", qualifiedByName = "discountId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ProductDiscountDTO toDto(ProductDiscount s);

    @Named("discountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DiscountDTO toDtoDiscountId(Discount discount);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
