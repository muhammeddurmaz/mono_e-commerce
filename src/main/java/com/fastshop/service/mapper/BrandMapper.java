package com.fastshop.service.mapper;

import com.fastshop.domain.Brand;
import com.fastshop.domain.Seller;
import com.fastshop.service.dto.BrandDTO;
import com.fastshop.service.dto.SellerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {
    @Mapping(target = "seller", source = "seller", qualifiedByName = "sellerId")
    BrandDTO toDto(Brand s);

    @Named("sellerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SellerDTO toDtoSellerId(Seller seller);
}
