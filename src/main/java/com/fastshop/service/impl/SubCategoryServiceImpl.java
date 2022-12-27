package com.fastshop.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.domain.SubCategory;
import com.fastshop.repository.SubCategoryRepository;
import com.fastshop.repository.search.SubCategorySearchRepository;
import com.fastshop.service.SubCategoryService;
import com.fastshop.service.dto.SubCategoryDTO;
import com.fastshop.service.mapper.SubCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubCategory}.
 */
@Service
@Transactional
public class SubCategoryServiceImpl implements SubCategoryService {

    private final Logger log = LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategoryMapper subCategoryMapper;

    private final SubCategorySearchRepository subCategorySearchRepository;

    public SubCategoryServiceImpl(
        SubCategoryRepository subCategoryRepository,
        SubCategoryMapper subCategoryMapper,
        SubCategorySearchRepository subCategorySearchRepository
    ) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategoryMapper = subCategoryMapper;
        this.subCategorySearchRepository = subCategorySearchRepository;
    }

    @Override
    public SubCategoryDTO save(SubCategoryDTO subCategoryDTO) {
        log.debug("Request to save SubCategory : {}", subCategoryDTO);
        SubCategory subCategory = subCategoryMapper.toEntity(subCategoryDTO);
        subCategory = subCategoryRepository.save(subCategory);
        SubCategoryDTO result = subCategoryMapper.toDto(subCategory);
        subCategorySearchRepository.index(subCategory);
        return result;
    }

    @Override
    public SubCategoryDTO update(SubCategoryDTO subCategoryDTO) {
        log.debug("Request to update SubCategory : {}", subCategoryDTO);
        SubCategory subCategory = subCategoryMapper.toEntity(subCategoryDTO);
        subCategory = subCategoryRepository.save(subCategory);
        SubCategoryDTO result = subCategoryMapper.toDto(subCategory);
        subCategorySearchRepository.index(subCategory);
        return result;
    }

    @Override
    public Optional<SubCategoryDTO> partialUpdate(SubCategoryDTO subCategoryDTO) {
        log.debug("Request to partially update SubCategory : {}", subCategoryDTO);

        return subCategoryRepository
            .findById(subCategoryDTO.getId())
            .map(existingSubCategory -> {
                subCategoryMapper.partialUpdate(existingSubCategory, subCategoryDTO);

                return existingSubCategory;
            })
            .map(subCategoryRepository::save)
            .map(savedSubCategory -> {
                subCategorySearchRepository.save(savedSubCategory);

                return savedSubCategory;
            })
            .map(subCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubCategories");
        return subCategoryRepository.findAll(pageable).map(subCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubCategoryDTO> findOne(Long id) {
        log.debug("Request to get SubCategory : {}", id);
        return subCategoryRepository.findById(id).map(subCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubCategory : {}", id);
        subCategoryRepository.deleteById(id);
        subCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SubCategories for query {}", query);
        return subCategorySearchRepository.search(query, pageable).map(subCategoryMapper::toDto);
    }
}
