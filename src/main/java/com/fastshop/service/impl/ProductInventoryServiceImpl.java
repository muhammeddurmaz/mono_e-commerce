package com.fastshop.service.impl;

import com.fastshop.domain.ProductInventory;
import com.fastshop.repository.ProductInventoryRepository;
import com.fastshop.service.ProductInventoryService;
import com.fastshop.service.dto.ProductInventoryDTO;
import com.fastshop.service.mapper.ProductInventoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductInventory}.
 */
@Service
@Transactional
public class ProductInventoryServiceImpl implements ProductInventoryService {

    private final Logger log = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);

    private final ProductInventoryRepository productInventoryRepository;

    private final ProductInventoryMapper productInventoryMapper;

    public ProductInventoryServiceImpl(
        ProductInventoryRepository productInventoryRepository,
        ProductInventoryMapper productInventoryMapper
    ) {
        this.productInventoryRepository = productInventoryRepository;
        this.productInventoryMapper = productInventoryMapper;
    }

    @Override
    public ProductInventoryDTO save(ProductInventoryDTO productInventoryDTO) {
        log.debug("Request to save ProductInventory : {}", productInventoryDTO);
        ProductInventory productInventory = productInventoryMapper.toEntity(productInventoryDTO);
        productInventory = productInventoryRepository.save(productInventory);
        return productInventoryMapper.toDto(productInventory);
    }

    @Override
    public ProductInventoryDTO update(ProductInventoryDTO productInventoryDTO) {
        log.debug("Request to update ProductInventory : {}", productInventoryDTO);
        ProductInventory productInventory = productInventoryMapper.toEntity(productInventoryDTO);
        productInventory = productInventoryRepository.save(productInventory);
        return productInventoryMapper.toDto(productInventory);
    }

    @Override
    public Optional<ProductInventoryDTO> partialUpdate(ProductInventoryDTO productInventoryDTO) {
        log.debug("Request to partially update ProductInventory : {}", productInventoryDTO);

        return productInventoryRepository
            .findById(productInventoryDTO.getId())
            .map(existingProductInventory -> {
                productInventoryMapper.partialUpdate(existingProductInventory, productInventoryDTO);

                return existingProductInventory;
            })
            .map(productInventoryRepository::save)
            .map(productInventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductInventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductInventories");
        return productInventoryRepository.findAll(pageable).map(productInventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductInventoryDTO> findOne(Long id) {
        log.debug("Request to get ProductInventory : {}", id);
        return productInventoryRepository.findById(id).map(productInventoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductInventory : {}", id);
        productInventoryRepository.deleteById(id);
    }
}
