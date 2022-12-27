package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.ProductType;
import com.fastshop.repository.ProductTypeRepository;
import com.fastshop.repository.search.ProductTypeSearchRepository;
import com.fastshop.service.ProductTypeService;
import com.fastshop.service.dto.ProductTypeDTO;
import com.fastshop.service.mapper.ProductTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductType}.
 */
@Service
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService {

    private final Logger log = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeServiceImpl(
        ProductTypeRepository productTypeRepository,
        ProductTypeMapper productTypeMapper,
        ProductTypeSearchRepository productTypeSearchRepository
    ) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    @Override
    public ProductTypeDTO save(ProductTypeDTO productTypeDTO) {
        log.debug("Request to save ProductType : {}", productTypeDTO);
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        ProductTypeDTO result = productTypeMapper.toDto(productType);
        productTypeSearchRepository.index(productType);
        return result;
    }

    @Override
    public ProductTypeDTO update(ProductTypeDTO productTypeDTO) {
        log.debug("Request to update ProductType : {}", productTypeDTO);
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        ProductTypeDTO result = productTypeMapper.toDto(productType);
        productTypeSearchRepository.index(productType);
        return result;
    }

    @Override
    public Optional<ProductTypeDTO> partialUpdate(ProductTypeDTO productTypeDTO) {
        log.debug("Request to partially update ProductType : {}", productTypeDTO);

        return productTypeRepository
            .findById(productTypeDTO.getId())
            .map(existingProductType -> {
                productTypeMapper.partialUpdate(existingProductType, productTypeDTO);

                return existingProductType;
            })
            .map(productTypeRepository::save)
            .map(savedProductType -> {
                productTypeSearchRepository.save(savedProductType);

                return savedProductType;
            })
            .map(productTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductTypes");
        return productTypeRepository.findAll(pageable).map(productTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductTypeDTO> findOne(Long id) {
        log.debug("Request to get ProductType : {}", id);
        return productTypeRepository.findById(id).map(productTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductType : {}", id);
        productTypeRepository.deleteById(id);
        productTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductTypes for query {}", query);
        return productTypeSearchRepository.search(query, pageable).map(productTypeMapper::toDto);
    }
}
