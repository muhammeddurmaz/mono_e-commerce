package com.fastshop.service.mapper;

import com.fastshop.domain.Order;
import com.fastshop.domain.User;
import com.fastshop.domain.UserAdress;
import com.fastshop.service.dto.OrderDTO;
import com.fastshop.service.dto.UserAdressDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "adress", source = "adress", qualifiedByName = "userAdressId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    OrderDTO toDto(Order s);

    @Named("userAdressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAdressDTO toDtoUserAdressId(UserAdress userAdress);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
