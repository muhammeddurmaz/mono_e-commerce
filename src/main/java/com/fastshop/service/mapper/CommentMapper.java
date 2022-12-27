package com.fastshop.service.mapper;

import com.fastshop.domain.Comment;
import com.fastshop.domain.Product;
import com.fastshop.domain.User;
import com.fastshop.service.dto.CommentDTO;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    CommentDTO toDto(Comment s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
