package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.PropertyDes;
import com.fastshop.repository.PropertyDesRepository;
import com.fastshop.repository.search.PropertyDesSearchRepository;
import com.fastshop.service.PropertyDesService;
import com.fastshop.service.dto.PropertyDesDTO;
import com.fastshop.service.mapper.PropertyDesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PropertyDes}.
 */
@Service
@Transactional
public class PropertyDesServiceImpl implements PropertyDesService {

    private final Logger log = LoggerFactory.getLogger(PropertyDesServiceImpl.class);

    private final PropertyDesRepository propertyDesRepository;

    private final PropertyDesMapper propertyDesMapper;

    private final PropertyDesSearchRepository propertyDesSearchRepository;

    public PropertyDesServiceImpl(
        PropertyDesRepository propertyDesRepository,
        PropertyDesMapper propertyDesMapper,
        PropertyDesSearchRepository propertyDesSearchRepository
    ) {
        this.propertyDesRepository = propertyDesRepository;
        this.propertyDesMapper = propertyDesMapper;
        this.propertyDesSearchRepository = propertyDesSearchRepository;
    }

    @Override
    public PropertyDesDTO save(PropertyDesDTO propertyDesDTO) {
        log.debug("Request to save PropertyDes : {}", propertyDesDTO);
        PropertyDes propertyDes = propertyDesMapper.toEntity(propertyDesDTO);
        propertyDes = propertyDesRepository.save(propertyDes);
        PropertyDesDTO result = propertyDesMapper.toDto(propertyDes);
        propertyDesSearchRepository.index(propertyDes);
        return result;
    }

    @Override
    public PropertyDesDTO update(PropertyDesDTO propertyDesDTO) {
        log.debug("Request to update PropertyDes : {}", propertyDesDTO);
        PropertyDes propertyDes = propertyDesMapper.toEntity(propertyDesDTO);
        propertyDes = propertyDesRepository.save(propertyDes);
        PropertyDesDTO result = propertyDesMapper.toDto(propertyDes);
        propertyDesSearchRepository.index(propertyDes);
        return result;
    }

    @Override
    public Optional<PropertyDesDTO> partialUpdate(PropertyDesDTO propertyDesDTO) {
        log.debug("Request to partially update PropertyDes : {}", propertyDesDTO);

        return propertyDesRepository
            .findById(propertyDesDTO.getId())
            .map(existingPropertyDes -> {
                propertyDesMapper.partialUpdate(existingPropertyDes, propertyDesDTO);

                return existingPropertyDes;
            })
            .map(propertyDesRepository::save)
            .map(savedPropertyDes -> {
                propertyDesSearchRepository.save(savedPropertyDes);

                return savedPropertyDes;
            })
            .map(propertyDesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PropertyDes");
        return propertyDesRepository.findAll(pageable).map(propertyDesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyDesDTO> findOne(Long id) {
        log.debug("Request to get PropertyDes : {}", id);
        return propertyDesRepository.findById(id).map(propertyDesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PropertyDes : {}", id);
        propertyDesRepository.deleteById(id);
        propertyDesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PropertyDes for query {}", query);
        return propertyDesSearchRepository.search(query, pageable).map(propertyDesMapper::toDto);
    }
}
