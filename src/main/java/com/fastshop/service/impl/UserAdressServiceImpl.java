package com.fastshop.service.impl;

import com.fastshop.domain.UserAdress;
import com.fastshop.repository.UserAdressRepository;
import com.fastshop.service.UserAdressService;
import com.fastshop.service.dto.UserAdressDTO;
import com.fastshop.service.mapper.UserAdressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAdress}.
 */
@Service
@Transactional
public class UserAdressServiceImpl implements UserAdressService {

    private final Logger log = LoggerFactory.getLogger(UserAdressServiceImpl.class);

    private final UserAdressRepository userAdressRepository;

    private final UserAdressMapper userAdressMapper;

    public UserAdressServiceImpl(UserAdressRepository userAdressRepository, UserAdressMapper userAdressMapper) {
        this.userAdressRepository = userAdressRepository;
        this.userAdressMapper = userAdressMapper;
    }

    @Override
    public UserAdressDTO save(UserAdressDTO userAdressDTO) {
        log.debug("Request to save UserAdress : {}", userAdressDTO);
        UserAdress userAdress = userAdressMapper.toEntity(userAdressDTO);
        userAdress = userAdressRepository.save(userAdress);
        return userAdressMapper.toDto(userAdress);
    }

    @Override
    public UserAdressDTO update(UserAdressDTO userAdressDTO) {
        log.debug("Request to update UserAdress : {}", userAdressDTO);
        UserAdress userAdress = userAdressMapper.toEntity(userAdressDTO);
        userAdress = userAdressRepository.save(userAdress);
        return userAdressMapper.toDto(userAdress);
    }

    @Override
    public Optional<UserAdressDTO> partialUpdate(UserAdressDTO userAdressDTO) {
        log.debug("Request to partially update UserAdress : {}", userAdressDTO);

        return userAdressRepository
            .findById(userAdressDTO.getId())
            .map(existingUserAdress -> {
                userAdressMapper.partialUpdate(existingUserAdress, userAdressDTO);

                return existingUserAdress;
            })
            .map(userAdressRepository::save)
            .map(userAdressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAdresses");
        return userAdressRepository.findAll(pageable).map(userAdressMapper::toDto);
    }

    public Page<UserAdressDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userAdressRepository.findAllWithEagerRelationships(pageable).map(userAdressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAdressDTO> findOne(Long id) {
        log.debug("Request to get UserAdress : {}", id);
        return userAdressRepository.findOneWithEagerRelationships(id).map(userAdressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAdress : {}", id);
        userAdressRepository.deleteById(id);
    }
}
