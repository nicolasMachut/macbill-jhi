package fr.nicolasmachut.web.rest;

import fr.nicolasmachut.MacbillApp;

import fr.nicolasmachut.domain.WorkDay;
import fr.nicolasmachut.domain.User;
import fr.nicolasmachut.domain.Customer;
import fr.nicolasmachut.repository.WorkDayRepository;
import fr.nicolasmachut.service.WorkDayService;
import fr.nicolasmachut.service.dto.WorkDayDTO;
import fr.nicolasmachut.service.mapper.WorkDayMapper;
import fr.nicolasmachut.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static fr.nicolasmachut.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WorkDayResource REST controller.
 *
 * @see WorkDayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MacbillApp.class)
public class WorkDayResourceIntTest {

    private static final Boolean DEFAULT_ALL_DAY = false;
    private static final Boolean UPDATED_ALL_DAY = true;

    private static final Boolean DEFAULT_DRAGGABLE = false;
    private static final Boolean UPDATED_DRAGGABLE = true;

    private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TITLE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TITLE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private WorkDayRepository workDayRepository;

    @Autowired
    private WorkDayMapper workDayMapper;

    @Autowired
    private WorkDayService workDayService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWorkDayMockMvc;

    private WorkDay workDay;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkDayResource workDayResource = new WorkDayResource(workDayService);
        this.restWorkDayMockMvc = MockMvcBuilders.standaloneSetup(workDayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkDay createEntity(EntityManager em) {
        WorkDay workDay = new WorkDay()
            .allDay(DEFAULT_ALL_DAY)
            .draggable(DEFAULT_DRAGGABLE)
            .end(DEFAULT_END)
            .start(DEFAULT_START)
            .title(DEFAULT_TITLE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        workDay.setUser(user);
        // Add required entity
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        workDay.setCustomer(customer);
        return workDay;
    }

    @Before
    public void initTest() {
        workDay = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkDay() throws Exception {
        int databaseSizeBeforeCreate = workDayRepository.findAll().size();

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);
        restWorkDayMockMvc.perform(post("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeCreate + 1);
        WorkDay testWorkDay = workDayList.get(workDayList.size() - 1);
        assertThat(testWorkDay.isAllDay()).isEqualTo(DEFAULT_ALL_DAY);
        assertThat(testWorkDay.isDraggable()).isEqualTo(DEFAULT_DRAGGABLE);
        assertThat(testWorkDay.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testWorkDay.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testWorkDay.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createWorkDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workDayRepository.findAll().size();

        // Create the WorkDay with an existing ID
        workDay.setId(1L);
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkDayMockMvc.perform(post("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAllDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setAllDay(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc.perform(post("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDraggableIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setDraggable(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc.perform(post("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setEnd(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc.perform(post("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = workDayRepository.findAll().size();
        // set the field null
        workDay.setStart(null);

        // Create the WorkDay, which fails.
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        restWorkDayMockMvc.perform(post("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkDays() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        // Get all the workDayList
        restWorkDayMockMvc.perform(get("/api/work-days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].draggable").value(hasItem(DEFAULT_DRAGGABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void getWorkDay() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        // Get the workDay
        restWorkDayMockMvc.perform(get("/api/work-days/{id}", workDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workDay.getId().intValue()))
            .andExpect(jsonPath("$.allDay").value(DEFAULT_ALL_DAY.booleanValue()))
            .andExpect(jsonPath("$.draggable").value(DEFAULT_DRAGGABLE.booleanValue()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkDay() throws Exception {
        // Get the workDay
        restWorkDayMockMvc.perform(get("/api/work-days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkDay() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();

        // Update the workDay
        WorkDay updatedWorkDay = workDayRepository.findById(workDay.getId()).get();
        // Disconnect from session so that the updates on updatedWorkDay are not directly saved in db
        em.detach(updatedWorkDay);
        updatedWorkDay
            .allDay(UPDATED_ALL_DAY)
            .draggable(UPDATED_DRAGGABLE)
            .end(UPDATED_END)
            .start(UPDATED_START)
            .title(UPDATED_TITLE);
        WorkDayDTO workDayDTO = workDayMapper.toDto(updatedWorkDay);

        restWorkDayMockMvc.perform(put("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isOk());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
        WorkDay testWorkDay = workDayList.get(workDayList.size() - 1);
        assertThat(testWorkDay.isAllDay()).isEqualTo(UPDATED_ALL_DAY);
        assertThat(testWorkDay.isDraggable()).isEqualTo(UPDATED_DRAGGABLE);
        assertThat(testWorkDay.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testWorkDay.getStart()).isEqualTo(UPDATED_START);
        assertThat(testWorkDay.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkDay() throws Exception {
        int databaseSizeBeforeUpdate = workDayRepository.findAll().size();

        // Create the WorkDay
        WorkDayDTO workDayDTO = workDayMapper.toDto(workDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkDayMockMvc.perform(put("/api/work-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workDayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkDay in the database
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkDay() throws Exception {
        // Initialize the database
        workDayRepository.saveAndFlush(workDay);

        int databaseSizeBeforeDelete = workDayRepository.findAll().size();

        // Delete the workDay
        restWorkDayMockMvc.perform(delete("/api/work-days/{id}", workDay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkDay> workDayList = workDayRepository.findAll();
        assertThat(workDayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkDay.class);
        WorkDay workDay1 = new WorkDay();
        workDay1.setId(1L);
        WorkDay workDay2 = new WorkDay();
        workDay2.setId(workDay1.getId());
        assertThat(workDay1).isEqualTo(workDay2);
        workDay2.setId(2L);
        assertThat(workDay1).isNotEqualTo(workDay2);
        workDay1.setId(null);
        assertThat(workDay1).isNotEqualTo(workDay2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkDayDTO.class);
        WorkDayDTO workDayDTO1 = new WorkDayDTO();
        workDayDTO1.setId(1L);
        WorkDayDTO workDayDTO2 = new WorkDayDTO();
        assertThat(workDayDTO1).isNotEqualTo(workDayDTO2);
        workDayDTO2.setId(workDayDTO1.getId());
        assertThat(workDayDTO1).isEqualTo(workDayDTO2);
        workDayDTO2.setId(2L);
        assertThat(workDayDTO1).isNotEqualTo(workDayDTO2);
        workDayDTO1.setId(null);
        assertThat(workDayDTO1).isNotEqualTo(workDayDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(workDayMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(workDayMapper.fromId(null)).isNull();
    }
}
