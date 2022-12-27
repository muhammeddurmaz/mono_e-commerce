package com.fastshop.service.mapper;

import com.fastshop.domain.Order;
import com.fastshop.domain.Payment;
import com.fastshop.domain.User;
import com.fastshop.domain.UserCart;
import com.fastshop.service.dto.OrderDTO;
import com.fastshop.service.dto.PaymentDTO;
import com.fastshop.service.dto.UserCartDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "usercart", source = "usercart", qualifiedByName = "userCartId")
    PaymentDTO toDto(Payment s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userCartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserCartDTO toDtoUserCartId(UserCart userCart);
}
