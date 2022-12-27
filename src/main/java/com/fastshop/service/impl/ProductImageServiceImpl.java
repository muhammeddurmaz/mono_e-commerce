package com.fastshop.service.impl;

import com.fastshop.domain.ProductImage;
import com.fastshop.repository.ProductImageRepository;
import com.fastshop.service.ProductImageService;
import com.fastshop.service.dto.ProductImageDTO;
import com.fastshop.service.mapper.ProductImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductImage}.
 */
@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final Logger log = LoggerFactory.getLogger(ProductImageServiceImpl.class);

    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ProductImageMapper productImageMapper) {
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public ProductImageDTO save(ProductImageDTO productImageDTO) {
        log.debug("Request to save ProductImage : {}", productImageDTO);
        ProductImage productImage = productImageMapper.toEntity(productImageDTO);
        productImage = productImageRepository.save(productImage);
        return productImageMapper.toDto(productImage);
    }

    @Override
    public ProductImageDTO update(ProductImageDTO productImageDTO) {
        log.debug("Request to update ProductImage : {}", productImageDTO);
        ProductImage productImage = productImageMapper.toEntity(productImageDTO);
        productImage = productImageRepository.save(productImage);
        return productImageMapper.toDto(productImage);
    }

    @Override
    public Optional<ProductImageDTO> partialUpdate(ProductImageDTO productImageDTO) {
        log.debug("Request to partially update ProductImage : {}", productImageDTO);

        return productImageRepository
            .findById(productImageDTO.getId())
            .map(existingProductImage -> {
                productImageMapper.partialUpdate(existingProductImage, productImageDTO);

                return existingProductImage;
            })
            .map(productImageRepository::save)
            .map(productImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductImages");
        return productImageRepository.findAll(pageable).map(productImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductImageDTO> findOne(Long id) {
        log.debug("Request to get ProductImage : {}", id);
        return productImageRepository.findById(id).map(productImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductImage : {}", id);
        productImageRepository.deleteById(id);
    }
}
