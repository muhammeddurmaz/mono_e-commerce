package com.fastshop.service.mapper;

import com.fastshop.domain.User;
import com.fastshop.domain.UserCart;
import com.fastshop.service.dto.UserCartDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserCart} and its DTO {@link UserCartDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserCartMapper extends EntityMapper<UserCartDTO, UserCart> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UserCartDTO toDto(UserCart s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
