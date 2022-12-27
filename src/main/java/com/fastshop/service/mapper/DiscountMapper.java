package com.fastshop.service.mapper;

import com.fastshop.domain.Discount;
import com.fastshop.service.dto.DiscountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Discount} and its DTO {@link DiscountDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiscountMapper extends EntityMapper<DiscountDTO, Discount> {}
