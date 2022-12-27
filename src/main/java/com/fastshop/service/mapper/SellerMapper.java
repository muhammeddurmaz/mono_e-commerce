package com.fastshop.service.mapper;

import com.fastshop.domain.ProductType;
import com.fastshop.domain.Seller;
import com.fastshop.domain.User;
import com.fastshop.service.dto.ProductTypeDTO;
import com.fastshop.service.dto.SellerDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Seller} and its DTO {@link SellerDTO}.
 */
@Mapper(componentModel = "spring")
public interface SellerMapper extends EntityMapper<SellerDTO, Seller> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "sellerProductType", source = "sellerProductType", qualifiedByName = "productTypeId")
    SellerDTO toDto(Seller s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("productTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductTypeDTO toDtoProductTypeId(ProductType productType);
}
