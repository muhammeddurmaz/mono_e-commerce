package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.Property;
import com.fastshop.repository.PropertyRepository;
import com.fastshop.repository.search.PropertySearchRepository;
import com.fastshop.service.PropertyService;
import com.fastshop.service.dto.PropertyDTO;
import com.fastshop.service.mapper.PropertyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Property}.
 */
@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);

    private final PropertyRepository propertyRepository;

    private final PropertyMapper propertyMapper;

    private final PropertySearchRepository propertySearchRepository;

    public PropertyServiceImpl(
        PropertyRepository propertyRepository,
        PropertyMapper propertyMapper,
        PropertySearchRepository propertySearchRepository
    ) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.propertySearchRepository = propertySearchRepository;
    }

    @Override
    public PropertyDTO save(PropertyDTO propertyDTO) {
        log.debug("Request to save Property : {}", propertyDTO);
        Property property = propertyMapper.toEntity(propertyDTO);
        property = propertyRepository.save(property);
        PropertyDTO result = propertyMapper.toDto(property);
        propertySearchRepository.index(property);
        return result;
    }

    @Override
    public PropertyDTO update(PropertyDTO propertyDTO) {
        log.debug("Request to update Property : {}", propertyDTO);
        Property property = propertyMapper.toEntity(propertyDTO);
        property = propertyRepository.save(property);
        PropertyDTO result = propertyMapper.toDto(property);
        propertySearchRepository.index(property);
        return result;
    }

    @Override
    public Optional<PropertyDTO> partialUpdate(PropertyDTO propertyDTO) {
        log.debug("Request to partially update Property : {}", propertyDTO);

        return propertyRepository
            .findById(propertyDTO.getId())
            .map(existingProperty -> {
                propertyMapper.partialUpdate(existingProperty, propertyDTO);

                return existingProperty;
            })
            .map(propertyRepository::save)
            .map(savedProperty -> {
                propertySearchRepository.save(savedProperty);

                return savedProperty;
            })
            .map(propertyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Properties");
        return propertyRepository.findAll(pageable).map(propertyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyDTO> findOne(Long id) {
        log.debug("Request to get Property : {}", id);
        return propertyRepository.findById(id).map(propertyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Property : {}", id);
        propertyRepository.deleteById(id);
        propertySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Properties for query {}", query);
        return propertySearchRepository.search(query, pageable).map(propertyMapper::toDto);
    }
}
