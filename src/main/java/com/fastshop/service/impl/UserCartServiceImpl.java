package com.fastshop.service.impl;

import com.fastshop.domain.UserCart;
import com.fastshop.repository.UserCartRepository;
import com.fastshop.service.UserCartService;
import com.fastshop.service.dto.UserCartDTO;
import com.fastshop.service.mapper.UserCartMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserCart}.
 */
@Service
@Transactional
public class UserCartServiceImpl implements UserCartService {

    private final Logger log = LoggerFactory.getLogger(UserCartServiceImpl.class);

    private final UserCartRepository userCartRepository;

    private final UserCartMapper userCartMapper;

    public UserCartServiceImpl(UserCartRepository userCartRepository, UserCartMapper userCartMapper) {
        this.userCartRepository = userCartRepository;
        this.userCartMapper = userCartMapper;
    }

    @Override
    public UserCartDTO save(UserCartDTO userCartDTO) {
        log.debug("Request to save UserCart : {}", userCartDTO);
        UserCart userCart = userCartMapper.toEntity(userCartDTO);
        userCart = userCartRepository.save(userCart);
        return userCartMapper.toDto(userCart);
    }

    @Override
    public UserCartDTO update(UserCartDTO userCartDTO) {
        log.debug("Request to update UserCart : {}", userCartDTO);
        UserCart userCart = userCartMapper.toEntity(userCartDTO);
        userCart = userCartRepository.save(userCart);
        return userCartMapper.toDto(userCart);
    }

    @Override
    public Optional<UserCartDTO> partialUpdate(UserCartDTO userCartDTO) {
        log.debug("Request to partially update UserCart : {}", userCartDTO);

        return userCartRepository
            .findById(userCartDTO.getId())
            .map(existingUserCart -> {
                userCartMapper.partialUpdate(existingUserCart, userCartDTO);

                return existingUserCart;
            })
            .map(userCartRepository::save)
            .map(userCartMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserCartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserCarts");
        return userCartRepository.findAll(pageable).map(userCartMapper::toDto);
    }

    public Page<UserCartDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userCartRepository.findAllWithEagerRelationships(pageable).map(userCartMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserCartDTO> findOne(Long id) {
        log.debug("Request to get UserCart : {}", id);
        return userCartRepository.findOneWithEagerRelationships(id).map(userCartMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserCart : {}", id);
        userCartRepository.deleteById(id);
    }
}
