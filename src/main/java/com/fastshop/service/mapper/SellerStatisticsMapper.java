package com.fastshop.service.mapper;

import com.fastshop.domain.Seller;
import com.fastshop.domain.SellerStatistics;
import com.fastshop.service.dto.SellerDTO;
import com.fastshop.service.dto.SellerStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SellerStatistics} and its DTO {@link SellerStatisticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SellerStatisticsMapper extends EntityMapper<SellerStatisticsDTO, SellerStatistics> {
    @Mapping(target = "seller", source = "seller", qualifiedByName = "sellerId")
    SellerStatisticsDTO toDto(SellerStatistics s);

    @Named("sellerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SellerDTO toDtoSellerId(Seller seller);
}
