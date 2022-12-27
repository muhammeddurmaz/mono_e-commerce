package com.fastshop.service.mapper;

import com.fastshop.domain.Brand;
import com.fastshop.domain.Category;
import com.fastshop.domain.Color;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductModel;
import com.fastshop.domain.Seller;
import com.fastshop.domain.SubCategory;
import com.fastshop.service.dto.BrandDTO;
import com.fastshop.service.dto.CategoryDTO;
import com.fastshop.service.dto.ColorDTO;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.dto.ProductModelDTO;
import com.fastshop.service.dto.SellerDTO;
import com.fastshop.service.dto.SubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "subCategoryId")
    @Mapping(target = "color", source = "color", qualifiedByName = "colorId")
    @Mapping(target = "productModel", source = "productModel", qualifiedByName = "productModelId")
    @Mapping(target = "seller", source = "seller", qualifiedByName = "sellerId")
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandId")
    ProductDTO toDto(Product s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("subCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubCategoryDTO toDtoSubCategoryId(SubCategory subCategory);

    @Named("colorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ColorDTO toDtoColorId(Color color);

    @Named("productModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductModelDTO toDtoProductModelId(ProductModel productModel);

    @Named("sellerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SellerDTO toDtoSellerId(Seller seller);

    @Named("brandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BrandDTO toDtoBrandId(Brand brand);
}
