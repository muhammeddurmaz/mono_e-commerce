package com.fastshop.service.impl;

import com.fastshop.domain.ProductModel;
import com.fastshop.repository.ProductModelRepository;
import com.fastshop.service.ProductModelService;
import com.fastshop.service.dto.ProductModelDTO;
import com.fastshop.service.mapper.ProductModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductModel}.
 */
@Service
@Transactional
public class ProductModelServiceImpl implements ProductModelService {

    private final Logger log = LoggerFactory.getLogger(ProductModelServiceImpl.class);

    private final ProductModelRepository productModelRepository;

    private final ProductModelMapper productModelMapper;

    public ProductModelServiceImpl(ProductModelRepository productModelRepository, ProductModelMapper productModelMapper) {
        this.productModelRepository = productModelRepository;
        this.productModelMapper = productModelMapper;
    }

    @Override
    public ProductModelDTO save(ProductModelDTO productModelDTO) {
        log.debug("Request to save ProductModel : {}", productModelDTO);
        ProductModel productModel = productModelMapper.toEntity(productModelDTO);
        productModel = productModelRepository.save(productModel);
        return productModelMapper.toDto(productModel);
    }

    @Override
    public ProductModelDTO update(ProductModelDTO productModelDTO) {
        log.debug("Request to update ProductModel : {}", productModelDTO);
        ProductModel productModel = productModelMapper.toEntity(productModelDTO);
        productModel = productModelRepository.save(productModel);
        return productModelMapper.toDto(productModel);
    }

    @Override
    public Optional<ProductModelDTO> partialUpdate(ProductModelDTO productModelDTO) {
        log.debug("Request to partially update ProductModel : {}", productModelDTO);

        return productModelRepository
            .findById(productModelDTO.getId())
            .map(existingProductModel -> {
                productModelMapper.partialUpdate(existingProductModel, productModelDTO);

                return existingProductModel;
            })
            .map(productModelRepository::save)
            .map(productModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductModels");
        return productModelRepository.findAll(pageable).map(productModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductModelDTO> findOne(Long id) {
        log.debug("Request to get ProductModel : {}", id);
        return productModelRepository.findById(id).map(productModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductModel : {}", id);
        productModelRepository.deleteById(id);
    }
}
