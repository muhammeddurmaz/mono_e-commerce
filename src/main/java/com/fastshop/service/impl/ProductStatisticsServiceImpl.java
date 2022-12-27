package com.fastshop.service.impl;

import com.fastshop.domain.ProductStatistics;
import com.fastshop.repository.ProductStatisticsRepository;
import com.fastshop.service.ProductStatisticsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductStatistics}.
 */
@Service
@Transactional
public class ProductStatisticsServiceImpl implements ProductStatisticsService {

    private final Logger log = LoggerFactory.getLogger(ProductStatisticsServiceImpl.class);

    private final ProductStatisticsRepository productStatisticsRepository;

    public ProductStatisticsServiceImpl(ProductStatisticsRepository productStatisticsRepository) {
        this.productStatisticsRepository = productStatisticsRepository;
    }

    @Override
    public ProductStatistics save(ProductStatistics productStatistics) {
        log.debug("Request to save ProductStatistics : {}", productStatistics);
        return productStatisticsRepository.save(productStatistics);
    }

    @Override
    public ProductStatistics update(ProductStatistics productStatistics) {
        log.debug("Request to update ProductStatistics : {}", productStatistics);
        return productStatisticsRepository.save(productStatistics);
    }

    @Override
    public Optional<ProductStatistics> partialUpdate(ProductStatistics productStatistics) {
        log.debug("Request to partially update ProductStatistics : {}", productStatistics);

        return productStatisticsRepository
            .findById(productStatistics.getId())
            .map(existingProductStatistics -> {
                if (productStatistics.getOrder() != null) {
                    existingProductStatistics.setOrder(productStatistics.getOrder());
                }
                if (productStatistics.getClick() != null) {
                    existingProductStatistics.setClick(productStatistics.getClick());
                }
                if (productStatistics.getComment() != null) {
                    existingProductStatistics.setComment(productStatistics.getComment());
                }
                if (productStatistics.getRating() != null) {
                    existingProductStatistics.setRating(productStatistics.getRating());
                }
                if (productStatistics.getAddCart() != null) {
                    existingProductStatistics.setAddCart(productStatistics.getAddCart());
                }

                return existingProductStatistics;
            })
            .map(productStatisticsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductStatistics> findAll(Pageable pageable) {
        log.debug("Request to get all ProductStatistics");
        return productStatisticsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductStatistics> findOne(Long id) {
        log.debug("Request to get ProductStatistics : {}", id);
        return productStatisticsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductStatistics : {}", id);
        productStatisticsRepository.deleteById(id);
    }
}
