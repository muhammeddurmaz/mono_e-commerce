package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.Discount;
import com.fastshop.repository.DiscountRepository;
import com.fastshop.repository.search.DiscountSearchRepository;
import com.fastshop.service.DiscountService;
import com.fastshop.service.dto.DiscountDTO;
import com.fastshop.service.mapper.DiscountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Discount}.
 */
@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

    private final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);

    private final DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    private final DiscountSearchRepository discountSearchRepository;

    public DiscountServiceImpl(
        DiscountRepository discountRepository,
        DiscountMapper discountMapper,
        DiscountSearchRepository discountSearchRepository
    ) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
        this.discountSearchRepository = discountSearchRepository;
    }

    @Override
    public DiscountDTO save(DiscountDTO discountDTO) {
        log.debug("Request to save Discount : {}", discountDTO);
        Discount discount = discountMapper.toEntity(discountDTO);
        discount = discountRepository.save(discount);
        DiscountDTO result = discountMapper.toDto(discount);
        discountSearchRepository.index(discount);
        return result;
    }

    @Override
    public DiscountDTO update(DiscountDTO discountDTO) {
        log.debug("Request to update Discount : {}", discountDTO);
        Discount discount = discountMapper.toEntity(discountDTO);
        discount = discountRepository.save(discount);
        DiscountDTO result = discountMapper.toDto(discount);
        discountSearchRepository.index(discount);
        return result;
    }

    @Override
    public Optional<DiscountDTO> partialUpdate(DiscountDTO discountDTO) {
        log.debug("Request to partially update Discount : {}", discountDTO);

        return discountRepository
            .findById(discountDTO.getId())
            .map(existingDiscount -> {
                discountMapper.partialUpdate(existingDiscount, discountDTO);

                return existingDiscount;
            })
            .map(discountRepository::save)
            .map(savedDiscount -> {
                discountSearchRepository.save(savedDiscount);

                return savedDiscount;
            })
            .map(discountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiscountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Discounts");
        return discountRepository.findAll(pageable).map(discountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiscountDTO> findOne(Long id) {
        log.debug("Request to get Discount : {}", id);
        return discountRepository.findById(id).map(discountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Discount : {}", id);
        discountRepository.deleteById(id);
        discountSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiscountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Discounts for query {}", query);
        return discountSearchRepository.search(query, pageable).map(discountMapper::toDto);
    }
}
