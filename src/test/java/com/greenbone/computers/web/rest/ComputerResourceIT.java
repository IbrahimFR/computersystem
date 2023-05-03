package com.greenbone.computers.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.greenbone.computers.IntegrationTest;
import com.greenbone.computers.domain.Computer;
import com.greenbone.computers.repository.ComputerRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ComputerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComputerResourceIT {

    private static final String DEFAULT_MAC_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_MAC_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_ABBREVIATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/computers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComputerMockMvc;

    private Computer computer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Computer createEntity(EntityManager em) {
        Computer computer = new Computer()
            .macAddress(DEFAULT_MAC_ADDRESS)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .employeeAbbreviation(DEFAULT_EMPLOYEE_ABBREVIATION)
            .description(DEFAULT_DESCRIPTION);
        return computer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Computer createUpdatedEntity(EntityManager em) {
        Computer computer = new Computer()
            .macAddress(UPDATED_MAC_ADDRESS)
            .ipAddress(UPDATED_IP_ADDRESS)
            .employeeAbbreviation(UPDATED_EMPLOYEE_ABBREVIATION)
            .description(UPDATED_DESCRIPTION);
        return computer;
    }

    @BeforeEach
    public void initTest() {
        computer = createEntity(em);
    }

    @Test
    @Transactional
    void createComputer() throws Exception {
        int databaseSizeBeforeCreate = computerRepository.findAll().size();
        // Create the Computer
        restComputerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computer)))
            .andExpect(status().isCreated());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeCreate + 1);
        Computer testComputer = computerList.get(computerList.size() - 1);
        assertThat(testComputer.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testComputer.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testComputer.getEmployeeAbbreviation()).isEqualTo(DEFAULT_EMPLOYEE_ABBREVIATION);
        assertThat(testComputer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createComputerWithExistingId() throws Exception {
        // Create the Computer with an existing ID
        computer.setId(1L);

        int databaseSizeBeforeCreate = computerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComputerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computer)))
            .andExpect(status().isBadRequest());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComputers() throws Exception {
        // Initialize the database
        computerRepository.saveAndFlush(computer);

        // Get all the computerList
        restComputerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(computer.getId().intValue())))
            .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].employeeAbbreviation").value(hasItem(DEFAULT_EMPLOYEE_ABBREVIATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getComputer() throws Exception {
        // Initialize the database
        computerRepository.saveAndFlush(computer);

        // Get the computer
        restComputerMockMvc
            .perform(get(ENTITY_API_URL_ID, computer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(computer.getId().intValue()))
            .andExpect(jsonPath("$.macAddress").value(DEFAULT_MAC_ADDRESS))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.employeeAbbreviation").value(DEFAULT_EMPLOYEE_ABBREVIATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingComputer() throws Exception {
        // Get the computer
        restComputerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComputer() throws Exception {
        // Initialize the database
        computerRepository.saveAndFlush(computer);

        int databaseSizeBeforeUpdate = computerRepository.findAll().size();

        // Update the computer
        Computer updatedComputer = computerRepository.findById(computer.getId()).get();
        // Disconnect from session so that the updates on updatedComputer are not directly saved in db
        em.detach(updatedComputer);
        updatedComputer
            .macAddress(UPDATED_MAC_ADDRESS)
            .ipAddress(UPDATED_IP_ADDRESS)
            .employeeAbbreviation(UPDATED_EMPLOYEE_ABBREVIATION)
            .description(UPDATED_DESCRIPTION);

        restComputerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComputer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComputer))
            )
            .andExpect(status().isOk());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
        Computer testComputer = computerList.get(computerList.size() - 1);
        assertThat(testComputer.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testComputer.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testComputer.getEmployeeAbbreviation()).isEqualTo(UPDATED_EMPLOYEE_ABBREVIATION);
        assertThat(testComputer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingComputer() throws Exception {
        int databaseSizeBeforeUpdate = computerRepository.findAll().size();
        computer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, computer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComputer() throws Exception {
        int databaseSizeBeforeUpdate = computerRepository.findAll().size();
        computer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComputer() throws Exception {
        int databaseSizeBeforeUpdate = computerRepository.findAll().size();
        computer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComputerWithPatch() throws Exception {
        // Initialize the database
        computerRepository.saveAndFlush(computer);

        int databaseSizeBeforeUpdate = computerRepository.findAll().size();

        // Update the computer using partial update
        Computer partialUpdatedComputer = new Computer();
        partialUpdatedComputer.setId(computer.getId());

        partialUpdatedComputer.ipAddress(UPDATED_IP_ADDRESS).description(UPDATED_DESCRIPTION);

        restComputerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputer))
            )
            .andExpect(status().isOk());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
        Computer testComputer = computerList.get(computerList.size() - 1);
        assertThat(testComputer.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testComputer.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testComputer.getEmployeeAbbreviation()).isEqualTo(DEFAULT_EMPLOYEE_ABBREVIATION);
        assertThat(testComputer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateComputerWithPatch() throws Exception {
        // Initialize the database
        computerRepository.saveAndFlush(computer);

        int databaseSizeBeforeUpdate = computerRepository.findAll().size();

        // Update the computer using partial update
        Computer partialUpdatedComputer = new Computer();
        partialUpdatedComputer.setId(computer.getId());

        partialUpdatedComputer
            .macAddress(UPDATED_MAC_ADDRESS)
            .ipAddress(UPDATED_IP_ADDRESS)
            .employeeAbbreviation(UPDATED_EMPLOYEE_ABBREVIATION)
            .description(UPDATED_DESCRIPTION);

        restComputerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputer))
            )
            .andExpect(status().isOk());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
        Computer testComputer = computerList.get(computerList.size() - 1);
        assertThat(testComputer.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testComputer.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testComputer.getEmployeeAbbreviation()).isEqualTo(UPDATED_EMPLOYEE_ABBREVIATION);
        assertThat(testComputer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingComputer() throws Exception {
        int databaseSizeBeforeUpdate = computerRepository.findAll().size();
        computer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, computer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComputer() throws Exception {
        int databaseSizeBeforeUpdate = computerRepository.findAll().size();
        computer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComputer() throws Exception {
        int databaseSizeBeforeUpdate = computerRepository.findAll().size();
        computer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(computer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Computer in the database
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComputer() throws Exception {
        // Initialize the database
        computerRepository.saveAndFlush(computer);

        int databaseSizeBeforeDelete = computerRepository.findAll().size();

        // Delete the computer
        restComputerMockMvc
            .perform(delete(ENTITY_API_URL_ID, computer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Computer> computerList = computerRepository.findAll();
        assertThat(computerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
