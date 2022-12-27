package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.User;
import com.fastshop.domain.UserAdress;
import com.fastshop.repository.UserAdressRepository;
import com.fastshop.service.UserAdressService;
import com.fastshop.service.criteria.UserAdressCriteria;
import com.fastshop.service.dto.UserAdressDTO;
import com.fastshop.service.mapper.UserAdressMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link UserAdressResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserAdressResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-adresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAdressRepository userAdressRepository;

    @Mock
    private UserAdressRepository userAdressRepositoryMock;

    @Autowired
    private UserAdressMapper userAdressMapper;

    @Mock
    private UserAdressService userAdressServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAdressMockMvc;

    private UserAdress userAdress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAdress createEntity(EntityManager em) {
        UserAdress userAdress = new UserAdress()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .telephone(DEFAULT_TELEPHONE)
            .city(DEFAULT_CITY)
            .adress(DEFAULT_ADRESS)
            .adressTitle(DEFAULT_ADRESS_TITLE);
        return userAdress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAdress createUpdatedEntity(EntityManager em) {
        UserAdress userAdress = new UserAdress()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .telephone(UPDATED_TELEPHONE)
            .city(UPDATED_CITY)
            .adress(UPDATED_ADRESS)
            .adressTitle(UPDATED_ADRESS_TITLE);
        return userAdress;
    }

    @BeforeEach
    public void initTest() {
        userAdress = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAdress() throws Exception {
        int databaseSizeBeforeCreate = userAdressRepository.findAll().size();
        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);
        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isCreated());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeCreate + 1);
        UserAdress testUserAdress = userAdressList.get(userAdressList.size() - 1);
        assertThat(testUserAdress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserAdress.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserAdress.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testUserAdress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserAdress.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testUserAdress.getAdressTitle()).isEqualTo(DEFAULT_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void createUserAdressWithExistingId() throws Exception {
        // Create the UserAdress with an existing ID
        userAdress.setId(1L);
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        int databaseSizeBeforeCreate = userAdressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAdressRepository.findAll().size();
        // set the field null
        userAdress.setName(null);

        // Create the UserAdress, which fails.
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isBadRequest());

        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAdressRepository.findAll().size();
        // set the field null
        userAdress.setLastName(null);

        // Create the UserAdress, which fails.
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isBadRequest());

        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAdressRepository.findAll().size();
        // set the field null
        userAdress.setTelephone(null);

        // Create the UserAdress, which fails.
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isBadRequest());

        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAdressRepository.findAll().size();
        // set the field null
        userAdress.setCity(null);

        // Create the UserAdress, which fails.
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isBadRequest());

        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdressTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAdressRepository.findAll().size();
        // set the field null
        userAdress.setAdressTitle(null);

        // Create the UserAdress, which fails.
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        restUserAdressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isBadRequest());

        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAdresses() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList
        restUserAdressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAdress.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].adressTitle").value(hasItem(DEFAULT_ADRESS_TITLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAdressesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userAdressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAdressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userAdressServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAdressesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userAdressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAdressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userAdressRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserAdress() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get the userAdress
        restUserAdressMockMvc
            .perform(get(ENTITY_API_URL_ID, userAdress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAdress.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS.toString()))
            .andExpect(jsonPath("$.adressTitle").value(DEFAULT_ADRESS_TITLE));
    }

    @Test
    @Transactional
    void getUserAdressesByIdFiltering() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        Long id = userAdress.getId();

        defaultUserAdressShouldBeFound("id.equals=" + id);
        defaultUserAdressShouldNotBeFound("id.notEquals=" + id);

        defaultUserAdressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserAdressShouldNotBeFound("id.greaterThan=" + id);

        defaultUserAdressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserAdressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserAdressesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where name equals to DEFAULT_NAME
        defaultUserAdressShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the userAdressList where name equals to UPDATED_NAME
        defaultUserAdressShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUserAdressShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the userAdressList where name equals to UPDATED_NAME
        defaultUserAdressShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where name is not null
        defaultUserAdressShouldBeFound("name.specified=true");

        // Get all the userAdressList where name is null
        defaultUserAdressShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAdressesByNameContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where name contains DEFAULT_NAME
        defaultUserAdressShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the userAdressList where name contains UPDATED_NAME
        defaultUserAdressShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where name does not contain DEFAULT_NAME
        defaultUserAdressShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the userAdressList where name does not contain UPDATED_NAME
        defaultUserAdressShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where lastName equals to DEFAULT_LAST_NAME
        defaultUserAdressShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the userAdressList where lastName equals to UPDATED_LAST_NAME
        defaultUserAdressShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUserAdressShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the userAdressList where lastName equals to UPDATED_LAST_NAME
        defaultUserAdressShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where lastName is not null
        defaultUserAdressShouldBeFound("lastName.specified=true");

        // Get all the userAdressList where lastName is null
        defaultUserAdressShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAdressesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where lastName contains DEFAULT_LAST_NAME
        defaultUserAdressShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the userAdressList where lastName contains UPDATED_LAST_NAME
        defaultUserAdressShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where lastName does not contain DEFAULT_LAST_NAME
        defaultUserAdressShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the userAdressList where lastName does not contain UPDATED_LAST_NAME
        defaultUserAdressShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserAdressesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where telephone equals to DEFAULT_TELEPHONE
        defaultUserAdressShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the userAdressList where telephone equals to UPDATED_TELEPHONE
        defaultUserAdressShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultUserAdressShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the userAdressList where telephone equals to UPDATED_TELEPHONE
        defaultUserAdressShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where telephone is not null
        defaultUserAdressShouldBeFound("telephone.specified=true");

        // Get all the userAdressList where telephone is null
        defaultUserAdressShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAdressesByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where telephone contains DEFAULT_TELEPHONE
        defaultUserAdressShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the userAdressList where telephone contains UPDATED_TELEPHONE
        defaultUserAdressShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where telephone does not contain DEFAULT_TELEPHONE
        defaultUserAdressShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the userAdressList where telephone does not contain UPDATED_TELEPHONE
        defaultUserAdressShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where city equals to DEFAULT_CITY
        defaultUserAdressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userAdressList where city equals to UPDATED_CITY
        defaultUserAdressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAdressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserAdressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userAdressList where city equals to UPDATED_CITY
        defaultUserAdressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAdressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where city is not null
        defaultUserAdressShouldBeFound("city.specified=true");

        // Get all the userAdressList where city is null
        defaultUserAdressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAdressesByCityContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where city contains DEFAULT_CITY
        defaultUserAdressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the userAdressList where city contains UPDATED_CITY
        defaultUserAdressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAdressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where city does not contain DEFAULT_CITY
        defaultUserAdressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the userAdressList where city does not contain UPDATED_CITY
        defaultUserAdressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAdressesByAdressTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where adressTitle equals to DEFAULT_ADRESS_TITLE
        defaultUserAdressShouldBeFound("adressTitle.equals=" + DEFAULT_ADRESS_TITLE);

        // Get all the userAdressList where adressTitle equals to UPDATED_ADRESS_TITLE
        defaultUserAdressShouldNotBeFound("adressTitle.equals=" + UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByAdressTitleIsInShouldWork() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where adressTitle in DEFAULT_ADRESS_TITLE or UPDATED_ADRESS_TITLE
        defaultUserAdressShouldBeFound("adressTitle.in=" + DEFAULT_ADRESS_TITLE + "," + UPDATED_ADRESS_TITLE);

        // Get all the userAdressList where adressTitle equals to UPDATED_ADRESS_TITLE
        defaultUserAdressShouldNotBeFound("adressTitle.in=" + UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByAdressTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where adressTitle is not null
        defaultUserAdressShouldBeFound("adressTitle.specified=true");

        // Get all the userAdressList where adressTitle is null
        defaultUserAdressShouldNotBeFound("adressTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAdressesByAdressTitleContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where adressTitle contains DEFAULT_ADRESS_TITLE
        defaultUserAdressShouldBeFound("adressTitle.contains=" + DEFAULT_ADRESS_TITLE);

        // Get all the userAdressList where adressTitle contains UPDATED_ADRESS_TITLE
        defaultUserAdressShouldNotBeFound("adressTitle.contains=" + UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByAdressTitleNotContainsSomething() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        // Get all the userAdressList where adressTitle does not contain DEFAULT_ADRESS_TITLE
        defaultUserAdressShouldNotBeFound("adressTitle.doesNotContain=" + DEFAULT_ADRESS_TITLE);

        // Get all the userAdressList where adressTitle does not contain UPDATED_ADRESS_TITLE
        defaultUserAdressShouldBeFound("adressTitle.doesNotContain=" + UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUserAdressesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userAdressRepository.saveAndFlush(userAdress);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        userAdress.setUser(user);
        userAdressRepository.saveAndFlush(userAdress);
        Long userId = user.getId();

        // Get all the userAdressList where user equals to userId
        defaultUserAdressShouldBeFound("userId.equals=" + userId);

        // Get all the userAdressList where user equals to (userId + 1)
        defaultUserAdressShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAdressShouldBeFound(String filter) throws Exception {
        restUserAdressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAdress.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].adressTitle").value(hasItem(DEFAULT_ADRESS_TITLE)));

        // Check, that the count call also returns 1
        restUserAdressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAdressShouldNotBeFound(String filter) throws Exception {
        restUserAdressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAdressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserAdress() throws Exception {
        // Get the userAdress
        restUserAdressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAdress() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();

        // Update the userAdress
        UserAdress updatedUserAdress = userAdressRepository.findById(userAdress.getId()).get();
        // Disconnect from session so that the updates on updatedUserAdress are not directly saved in db
        em.detach(updatedUserAdress);
        updatedUserAdress
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .telephone(UPDATED_TELEPHONE)
            .city(UPDATED_CITY)
            .adress(UPDATED_ADRESS)
            .adressTitle(UPDATED_ADRESS_TITLE);
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(updatedUserAdress);

        restUserAdressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAdressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAdressDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
        UserAdress testUserAdress = userAdressList.get(userAdressList.size() - 1);
        assertThat(testUserAdress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserAdress.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserAdress.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testUserAdress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserAdress.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testUserAdress.getAdressTitle()).isEqualTo(UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingUserAdress() throws Exception {
        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();
        userAdress.setId(count.incrementAndGet());

        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAdressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAdressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAdressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAdress() throws Exception {
        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();
        userAdress.setId(count.incrementAndGet());

        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAdressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAdressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAdress() throws Exception {
        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();
        userAdress.setId(count.incrementAndGet());

        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAdressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAdressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAdressWithPatch() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();

        // Update the userAdress using partial update
        UserAdress partialUpdatedUserAdress = new UserAdress();
        partialUpdatedUserAdress.setId(userAdress.getId());

        partialUpdatedUserAdress.telephone(UPDATED_TELEPHONE).city(UPDATED_CITY).adressTitle(UPDATED_ADRESS_TITLE);

        restUserAdressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAdress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAdress))
            )
            .andExpect(status().isOk());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
        UserAdress testUserAdress = userAdressList.get(userAdressList.size() - 1);
        assertThat(testUserAdress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserAdress.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserAdress.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testUserAdress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserAdress.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testUserAdress.getAdressTitle()).isEqualTo(UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateUserAdressWithPatch() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();

        // Update the userAdress using partial update
        UserAdress partialUpdatedUserAdress = new UserAdress();
        partialUpdatedUserAdress.setId(userAdress.getId());

        partialUpdatedUserAdress
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .telephone(UPDATED_TELEPHONE)
            .city(UPDATED_CITY)
            .adress(UPDATED_ADRESS)
            .adressTitle(UPDATED_ADRESS_TITLE);

        restUserAdressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAdress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAdress))
            )
            .andExpect(status().isOk());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
        UserAdress testUserAdress = userAdressList.get(userAdressList.size() - 1);
        assertThat(testUserAdress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserAdress.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserAdress.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testUserAdress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserAdress.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testUserAdress.getAdressTitle()).isEqualTo(UPDATED_ADRESS_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingUserAdress() throws Exception {
        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();
        userAdress.setId(count.incrementAndGet());

        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAdressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAdressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAdressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAdress() throws Exception {
        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();
        userAdress.setId(count.incrementAndGet());

        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAdressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAdressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAdress() throws Exception {
        int databaseSizeBeforeUpdate = userAdressRepository.findAll().size();
        userAdress.setId(count.incrementAndGet());

        // Create the UserAdress
        UserAdressDTO userAdressDTO = userAdressMapper.toDto(userAdress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAdressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAdressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAdress in the database
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAdress() throws Exception {
        // Initialize the database
        userAdressRepository.saveAndFlush(userAdress);

        int databaseSizeBeforeDelete = userAdressRepository.findAll().size();

        // Delete the userAdress
        restUserAdressMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAdress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAdress> userAdressList = userAdressRepository.findAll();
        assertThat(userAdressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
