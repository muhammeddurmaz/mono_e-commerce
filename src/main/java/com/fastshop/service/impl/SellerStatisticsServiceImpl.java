package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.SellerStatistics;
import com.fastshop.repository.SellerStatisticsRepository;
import com.fastshop.repository.search.SellerStatisticsSearchRepository;
import com.fastshop.service.SellerStatisticsService;
import com.fastshop.service.dto.SellerStatisticsDTO;
import com.fastshop.service.mapper.SellerStatisticsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SellerStatistics}.
 */
@Service
@Transactional
public class SellerStatisticsServiceImpl implements SellerStatisticsService {

    private final Logger log = LoggerFactory.getLogger(SellerStatisticsServiceImpl.class);

    private final SellerStatisticsRepository sellerStatisticsRepository;

    private final SellerStatisticsMapper sellerStatisticsMapper;

    private final SellerStatisticsSearchRepository sellerStatisticsSearchRepository;

    public SellerStatisticsServiceImpl(
        SellerStatisticsRepository sellerStatisticsRepository,
        SellerStatisticsMapper sellerStatisticsMapper,
        SellerStatisticsSearchRepository sellerStatisticsSearchRepository
    ) {
        this.sellerStatisticsRepository = sellerStatisticsRepository;
        this.sellerStatisticsMapper = sellerStatisticsMapper;
        this.sellerStatisticsSearchRepository = sellerStatisticsSearchRepository;
    }

    @Override
    public SellerStatisticsDTO save(SellerStatisticsDTO sellerStatisticsDTO) {
        log.debug("Request to save SellerStatistics : {}", sellerStatisticsDTO);
        SellerStatistics sellerStatistics = sellerStatisticsMapper.toEntity(sellerStatisticsDTO);
        sellerStatistics = sellerStatisticsRepository.save(sellerStatistics);
        SellerStatisticsDTO result = sellerStatisticsMapper.toDto(sellerStatistics);
        sellerStatisticsSearchRepository.index(sellerStatistics);
        return result;
    }

    @Override
    public SellerStatisticsDTO update(SellerStatisticsDTO sellerStatisticsDTO) {
        log.debug("Request to update SellerStatistics : {}", sellerStatisticsDTO);
        SellerStatistics sellerStatistics = sellerStatisticsMapper.toEntity(sellerStatisticsDTO);
        sellerStatistics = sellerStatisticsRepository.save(sellerStatistics);
        SellerStatisticsDTO result = sellerStatisticsMapper.toDto(sellerStatistics);
        sellerStatisticsSearchRepository.index(sellerStatistics);
        return result;
    }

    @Override
    public Optional<SellerStatisticsDTO> partialUpdate(SellerStatisticsDTO sellerStatisticsDTO) {
        log.debug("Request to partially update SellerStatistics : {}", sellerStatisticsDTO);

        return sellerStatisticsRepository
            .findById(sellerStatisticsDTO.getId())
            .map(existingSellerStatistics -> {
                sellerStatisticsMapper.partialUpdate(existingSellerStatistics, sellerStatisticsDTO);

                return existingSellerStatistics;
            })
            .map(sellerStatisticsRepository::save)
            .map(savedSellerStatistics -> {
                sellerStatisticsSearchRepository.save(savedSellerStatistics);

                return savedSellerStatistics;
            })
            .map(sellerStatisticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerStatisticsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SellerStatistics");
        return sellerStatisticsRepository.findAll(pageable).map(sellerStatisticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SellerStatisticsDTO> findOne(Long id) {
        log.debug("Request to get SellerStatistics : {}", id);
        return sellerStatisticsRepository.findById(id).map(sellerStatisticsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SellerStatistics : {}", id);
        sellerStatisticsRepository.deleteById(id);
        sellerStatisticsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerStatisticsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SellerStatistics for query {}", query);
        return sellerStatisticsSearchRepository.search(query, pageable).map(sellerStatisticsMapper::toDto);
    }
}
