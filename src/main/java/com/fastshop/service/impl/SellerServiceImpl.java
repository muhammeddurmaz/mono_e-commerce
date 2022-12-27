package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.Seller;
import com.fastshop.repository.SellerRepository;
import com.fastshop.repository.search.SellerSearchRepository;
import com.fastshop.service.SellerService;
import com.fastshop.service.dto.SellerDTO;
import com.fastshop.service.mapper.SellerMapper;
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
 * Service Implementation for managing {@link Seller}.
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;

    private final SellerSearchRepository sellerSearchRepository;

    public SellerServiceImpl(SellerRepository sellerRepository, SellerMapper sellerMapper, SellerSearchRepository sellerSearchRepository) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
        this.sellerSearchRepository = sellerSearchRepository;
    }

    @Override
    public SellerDTO save(SellerDTO sellerDTO) {
        log.debug("Request to save Seller : {}", sellerDTO);
        Seller seller = sellerMapper.toEntity(sellerDTO);
        seller = sellerRepository.save(seller);
        SellerDTO result = sellerMapper.toDto(seller);
        sellerSearchRepository.index(seller);
        return result;
    }

    @Override
    public SellerDTO update(SellerDTO sellerDTO) {
        log.debug("Request to update Seller : {}", sellerDTO);
        Seller seller = sellerMapper.toEntity(sellerDTO);
        seller = sellerRepository.save(seller);
        SellerDTO result = sellerMapper.toDto(seller);
        sellerSearchRepository.index(seller);
        return result;
    }

    @Override
    public Optional<SellerDTO> partialUpdate(SellerDTO sellerDTO) {
        log.debug("Request to partially update Seller : {}", sellerDTO);

        return sellerRepository
            .findById(sellerDTO.getId())
            .map(existingSeller -> {
                sellerMapper.partialUpdate(existingSeller, sellerDTO);

                return existingSeller;
            })
            .map(sellerRepository::save)
            .map(savedSeller -> {
                sellerSearchRepository.save(savedSeller);

                return savedSeller;
            })
            .map(sellerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sellers");
        return sellerRepository.findAll(pageable).map(sellerMapper::toDto);
    }

    /**
     *  Get all the sellers where Brand is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SellerDTO> findAllWhereBrandIsNull() {
        log.debug("Request to get all sellers where Brand is null");
        return StreamSupport
            .stream(sellerRepository.findAll().spliterator(), false)
            .filter(seller -> seller.getBrand() == null)
            .map(sellerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the sellers where SellerStatistics is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SellerDTO> findAllWhereSellerStatisticsIsNull() {
        log.debug("Request to get all sellers where SellerStatistics is null");
        return StreamSupport
            .stream(sellerRepository.findAll().spliterator(), false)
            .filter(seller -> seller.getSellerStatistics() == null)
            .map(sellerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SellerDTO> findOne(Long id) {
        log.debug("Request to get Seller : {}", id);
        return sellerRepository.findById(id).map(sellerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Seller : {}", id);
        sellerRepository.deleteById(id);
        sellerSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sellers for query {}", query);
        return sellerSearchRepository.search(query, pageable).map(sellerMapper::toDto);
    }
}
