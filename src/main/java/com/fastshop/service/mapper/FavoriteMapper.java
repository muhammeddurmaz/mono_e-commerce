package com.fastshop.service.mapper;

import com.fastshop.domain.Favorite;
import com.fastshop.domain.Product;
import com.fastshop.domain.User;
import com.fastshop.service.dto.FavoriteDTO;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Favorite} and its DTO {@link FavoriteDTO}.
 */
@Mapper(componentModel = "spring")
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    FavoriteDTO toDto(Favorite s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
