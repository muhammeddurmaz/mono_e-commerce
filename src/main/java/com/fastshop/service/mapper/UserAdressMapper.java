package com.fastshop.service.mapper;

import com.fastshop.domain.User;
import com.fastshop.domain.UserAdress;
import com.fastshop.service.dto.UserAdressDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAdress} and its DTO {@link UserAdressDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAdressMapper extends EntityMapper<UserAdressDTO, UserAdress> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UserAdressDTO toDto(UserAdress s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
