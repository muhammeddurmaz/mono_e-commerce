package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.ProductDiscount;
import com.fastshop.repository.ProductDiscountRepository;
import com.fastshop.repository.search.ProductDiscountSearchRepository;
import com.fastshop.service.ProductDiscountService;
import com.fastshop.service.dto.ProductDiscountDTO;
import com.fastshop.service.mapper.ProductDiscountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductDiscount}.
 */
@Service
@Transactional
public class ProductDiscountServiceImpl implements ProductDiscountService {

    private final Logger log = LoggerFactory.getLogger(ProductDiscountServiceImpl.class);

    private final ProductDiscountRepository productDiscountRepository;

    private final ProductDiscountMapper productDiscountMapper;

    private final ProductDiscountSearchRepository productDiscountSearchRepository;

    public ProductDiscountServiceImpl(
        ProductDiscountRepository productDiscountRepository,
        ProductDiscountMapper productDiscountMapper,
        ProductDiscountSearchRepository productDiscountSearchRepository
    ) {
        this.productDiscountRepository = productDiscountRepository;
        this.productDiscountMapper = productDiscountMapper;
        this.productDiscountSearchRepository = productDiscountSearchRepository;
    }

    @Override
    public ProductDiscountDTO save(ProductDiscountDTO productDiscountDTO) {
        log.debug("Request to save ProductDiscount : {}", productDiscountDTO);
        ProductDiscount productDiscount = productDiscountMapper.toEntity(productDiscountDTO);
        productDiscount = productDiscountRepository.save(productDiscount);
        ProductDiscountDTO result = productDiscountMapper.toDto(productDiscount);
        productDiscountSearchRepository.index(productDiscount);
        return result;
    }

    @Override
    public ProductDiscountDTO update(ProductDiscountDTO productDiscountDTO) {
        log.debug("Request to update ProductDiscount : {}", productDiscountDTO);
        ProductDiscount productDiscount = productDiscountMapper.toEntity(productDiscountDTO);
        productDiscount = productDiscountRepository.save(productDiscount);
        ProductDiscountDTO result = productDiscountMapper.toDto(productDiscount);
        productDiscountSearchRepository.index(productDiscount);
        return result;
    }

    @Override
    public Optional<ProductDiscountDTO> partialUpdate(ProductDiscountDTO productDiscountDTO) {
        log.debug("Request to partially update ProductDiscount : {}", productDiscountDTO);

        return productDiscountRepository
            .findById(productDiscountDTO.getId())
            .map(existingProductDiscount -> {
                productDiscountMapper.partialUpdate(existingProductDiscount, productDiscountDTO);

                return existingProductDiscount;
            })
            .map(productDiscountRepository::save)
            .map(savedProductDiscount -> {
                productDiscountSearchRepository.save(savedProductDiscount);

                return savedProductDiscount;
            })
            .map(productDiscountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDiscountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductDiscounts");
        return productDiscountRepository.findAll(pageable).map(productDiscountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDiscountDTO> findOne(Long id) {
        log.debug("Request to get ProductDiscount : {}", id);
        return productDiscountRepository.findById(id).map(productDiscountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductDiscount : {}", id);
        productDiscountRepository.deleteById(id);
        productDiscountSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDiscountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductDiscounts for query {}", query);
        return productDiscountSearchRepository.search(query, pageable).map(productDiscountMapper::toDto);
    }
}
