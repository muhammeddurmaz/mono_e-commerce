package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.UserCart;
import com.fastshop.repository.UserCartRepository;
import com.fastshop.service.UserCartService;
import com.fastshop.service.dto.UserCartDTO;
import com.fastshop.service.mapper.UserCartMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserCartResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserCartResourceIT {

    private static final String DEFAULT_CART_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CART_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CART_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CART_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SKT_AY = "AAAAAAAAAA";
    private static final String UPDATED_SKT_AY = "BBBBBBBBBB";

    private static final String DEFAULT_SKT_YIL = "AAAAAAAAAA";
    private static final String UPDATED_SKT_YIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCartRepository userCartRepository;

    @Mock
    private UserCartRepository userCartRepositoryMock;

    @Autowired
    private UserCartMapper userCartMapper;

    @Mock
    private UserCartService userCartServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCartMockMvc;

    private UserCart userCart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCart createEntity(EntityManager em) {
        UserCart userCart = new UserCart()
            .cartName(DEFAULT_CART_NAME)
            .cartNumber(DEFAULT_CART_NUMBER)
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .sktAy(DEFAULT_SKT_AY)
            .sktYil(DEFAULT_SKT_YIL);
        return userCart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCart createUpdatedEntity(EntityManager em) {
        UserCart userCart = new UserCart()
            .cartName(UPDATED_CART_NAME)
            .cartNumber(UPDATED_CART_NUMBER)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .sktAy(UPDATED_SKT_AY)
            .sktYil(UPDATED_SKT_YIL);
        return userCart;
    }

    @BeforeEach
    public void initTest() {
        userCart = createEntity(em);
    }

    @Test
    @Transactional
    void createUserCart() throws Exception {
        int databaseSizeBeforeCreate = userCartRepository.findAll().size();
        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);
        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isCreated());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeCreate + 1);
        UserCart testUserCart = userCartList.get(userCartList.size() - 1);
        assertThat(testUserCart.getCartName()).isEqualTo(DEFAULT_CART_NAME);
        assertThat(testUserCart.getCartNumber()).isEqualTo(DEFAULT_CART_NUMBER);
        assertThat(testUserCart.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserCart.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserCart.getSktAy()).isEqualTo(DEFAULT_SKT_AY);
        assertThat(testUserCart.getSktYil()).isEqualTo(DEFAULT_SKT_YIL);
    }

    @Test
    @Transactional
    void createUserCartWithExistingId() throws Exception {
        // Create the UserCart with an existing ID
        userCart.setId(1L);
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        int databaseSizeBeforeCreate = userCartRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCartNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCartRepository.findAll().size();
        // set the field null
        userCart.setCartName(null);

        // Create the UserCart, which fails.
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCartNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCartRepository.findAll().size();
        // set the field null
        userCart.setCartNumber(null);

        // Create the UserCart, which fails.
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCartRepository.findAll().size();
        // set the field null
        userCart.setName(null);

        // Create the UserCart, which fails.
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCartRepository.findAll().size();
        // set the field null
        userCart.setLastName(null);

        // Create the UserCart, which fails.
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSktAyIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCartRepository.findAll().size();
        // set the field null
        userCart.setSktAy(null);

        // Create the UserCart, which fails.
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSktYilIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCartRepository.findAll().size();
        // set the field null
        userCart.setSktYil(null);

        // Create the UserCart, which fails.
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        restUserCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isBadRequest());

        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserCarts() throws Exception {
        // Initialize the database
        userCartRepository.saveAndFlush(userCart);

        // Get all the userCartList
        restUserCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].cartName").value(hasItem(DEFAULT_CART_NAME)))
            .andExpect(jsonPath("$.[*].cartNumber").value(hasItem(DEFAULT_CART_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].sktAy").value(hasItem(DEFAULT_SKT_AY)))
            .andExpect(jsonPath("$.[*].sktYil").value(hasItem(DEFAULT_SKT_YIL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCartsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userCartServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserCartMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userCartServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCartsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userCartServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserCartMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userCartRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserCart() throws Exception {
        // Initialize the database
        userCartRepository.saveAndFlush(userCart);

        // Get the userCart
        restUserCartMockMvc
            .perform(get(ENTITY_API_URL_ID, userCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userCart.getId().intValue()))
            .andExpect(jsonPath("$.cartName").value(DEFAULT_CART_NAME))
            .andExpect(jsonPath("$.cartNumber").value(DEFAULT_CART_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.sktAy").value(DEFAULT_SKT_AY))
            .andExpect(jsonPath("$.sktYil").value(DEFAULT_SKT_YIL));
    }

    @Test
    @Transactional
    void getNonExistingUserCart() throws Exception {
        // Get the userCart
        restUserCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserCart() throws Exception {
        // Initialize the database
        userCartRepository.saveAndFlush(userCart);

        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();

        // Update the userCart
        UserCart updatedUserCart = userCartRepository.findById(userCart.getId()).get();
        // Disconnect from session so that the updates on updatedUserCart are not directly saved in db
        em.detach(updatedUserCart);
        updatedUserCart
            .cartName(UPDATED_CART_NAME)
            .cartNumber(UPDATED_CART_NUMBER)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .sktAy(UPDATED_SKT_AY)
            .sktYil(UPDATED_SKT_YIL);
        UserCartDTO userCartDTO = userCartMapper.toDto(updatedUserCart);

        restUserCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCartDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
        UserCart testUserCart = userCartList.get(userCartList.size() - 1);
        assertThat(testUserCart.getCartName()).isEqualTo(UPDATED_CART_NAME);
        assertThat(testUserCart.getCartNumber()).isEqualTo(UPDATED_CART_NUMBER);
        assertThat(testUserCart.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserCart.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserCart.getSktAy()).isEqualTo(UPDATED_SKT_AY);
        assertThat(testUserCart.getSktYil()).isEqualTo(UPDATED_SKT_YIL);
    }

    @Test
    @Transactional
    void putNonExistingUserCart() throws Exception {
        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();
        userCart.setId(count.incrementAndGet());

        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserCart() throws Exception {
        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();
        userCart.setId(count.incrementAndGet());

        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserCart() throws Exception {
        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();
        userCart.setId(count.incrementAndGet());

        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserCartWithPatch() throws Exception {
        // Initialize the database
        userCartRepository.saveAndFlush(userCart);

        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();

        // Update the userCart using partial update
        UserCart partialUpdatedUserCart = new UserCart();
        partialUpdatedUserCart.setId(userCart.getId());

        partialUpdatedUserCart.cartNumber(UPDATED_CART_NUMBER).name(UPDATED_NAME).lastName(UPDATED_LAST_NAME).sktAy(UPDATED_SKT_AY);

        restUserCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCart))
            )
            .andExpect(status().isOk());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
        UserCart testUserCart = userCartList.get(userCartList.size() - 1);
        assertThat(testUserCart.getCartName()).isEqualTo(DEFAULT_CART_NAME);
        assertThat(testUserCart.getCartNumber()).isEqualTo(UPDATED_CART_NUMBER);
        assertThat(testUserCart.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserCart.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserCart.getSktAy()).isEqualTo(UPDATED_SKT_AY);
        assertThat(testUserCart.getSktYil()).isEqualTo(DEFAULT_SKT_YIL);
    }

    @Test
    @Transactional
    void fullUpdateUserCartWithPatch() throws Exception {
        // Initialize the database
        userCartRepository.saveAndFlush(userCart);

        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();

        // Update the userCart using partial update
        UserCart partialUpdatedUserCart = new UserCart();
        partialUpdatedUserCart.setId(userCart.getId());

        partialUpdatedUserCart
            .cartName(UPDATED_CART_NAME)
            .cartNumber(UPDATED_CART_NUMBER)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .sktAy(UPDATED_SKT_AY)
            .sktYil(UPDATED_SKT_YIL);

        restUserCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCart))
            )
            .andExpect(status().isOk());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
        UserCart testUserCart = userCartList.get(userCartList.size() - 1);
        assertThat(testUserCart.getCartName()).isEqualTo(UPDATED_CART_NAME);
        assertThat(testUserCart.getCartNumber()).isEqualTo(UPDATED_CART_NUMBER);
        assertThat(testUserCart.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserCart.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserCart.getSktAy()).isEqualTo(UPDATED_SKT_AY);
        assertThat(testUserCart.getSktYil()).isEqualTo(UPDATED_SKT_YIL);
    }

    @Test
    @Transactional
    void patchNonExistingUserCart() throws Exception {
        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();
        userCart.setId(count.incrementAndGet());

        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userCartDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserCart() throws Exception {
        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();
        userCart.setId(count.incrementAndGet());

        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserCart() throws Exception {
        int databaseSizeBeforeUpdate = userCartRepository.findAll().size();
        userCart.setId(count.incrementAndGet());

        // Create the UserCart
        UserCartDTO userCartDTO = userCartMapper.toDto(userCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCartMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userCartDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCart in the database
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserCart() throws Exception {
        // Initialize the database
        userCartRepository.saveAndFlush(userCart);

        int databaseSizeBeforeDelete = userCartRepository.findAll().size();

        // Delete the userCart
        restUserCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, userCart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserCart> userCartList = userCartRepository.findAll();
        assertThat(userCartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
