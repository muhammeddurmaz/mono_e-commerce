package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.Product;
import com.fastshop.repository.ProductRepository;
import com.fastshop.repository.search.ProductSearchRepository;
import com.fastshop.service.ProductService;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.mapper.ProductMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductServiceImpl(
        ProductRepository productRepository,
        ProductMapper productMapper,
        ProductSearchRepository productSearchRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        productSearchRepository.index(product);
        return result;
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        log.debug("Request to update Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        productSearchRepository.index(product);
        return result;
    }

    @Override
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(savedProduct -> {
                productSearchRepository.save(savedProduct);

                return savedProduct;
            })
            .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    /**
     *  Get all the products where ProductStatistics is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllWhereProductStatisticsIsNull() {
        log.debug("Request to get all products where ProductStatistics is null");
        return StreamSupport
            .stream(productRepository.findAll().spliterator(), false)
            .filter(product -> product.getProductStatistics() == null)
            .map(productMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productSearchRepository.search(query, pageable).map(productMapper::toDto);
    }
}
