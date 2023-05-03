package com.greenbone.computers.web.rest;

import com.greenbone.computers.domain.Computer;
import com.greenbone.computers.repository.ComputerRepository;
import com.greenbone.computers.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.greenbone.computers.domain.Computer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComputerResource {

    private final Logger log = LoggerFactory.getLogger(ComputerResource.class);

    private static final String ENTITY_NAME = "computer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComputerRepository computerRepository;

    public ComputerResource(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    /**
     * {@code POST  /computers} : Create a new computer.
     *
     * @param computer the computer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new computer, or with status {@code 400 (Bad Request)} if the computer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/computers")
    public ResponseEntity<Computer> createComputer(@RequestBody Computer computer) throws URISyntaxException {
        log.debug("REST request to save Computer : {}", computer);
        if (computer.getId() != null) {
            throw new BadRequestAlertException("A new computer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Computer result = computerRepository.save(computer);
        return ResponseEntity
            .created(new URI("/api/computers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /computers/:id} : Updates an existing computer.
     *
     * @param id the id of the computer to save.
     * @param computer the computer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computer,
     * or with status {@code 400 (Bad Request)} if the computer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the computer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/computers/{id}")
    public ResponseEntity<Computer> updateComputer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Computer computer
    ) throws URISyntaxException {
        log.debug("REST request to update Computer : {}, {}", id, computer);
        if (computer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Computer result = computerRepository.save(computer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, computer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /computers/:id} : Partial updates given fields of an existing computer, field will ignore if it is null
     *
     * @param id the id of the computer to save.
     * @param computer the computer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computer,
     * or with status {@code 400 (Bad Request)} if the computer is not valid,
     * or with status {@code 404 (Not Found)} if the computer is not found,
     * or with status {@code 500 (Internal Server Error)} if the computer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/computers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Computer> partialUpdateComputer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Computer computer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Computer partially : {}, {}", id, computer);
        if (computer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Computer> result = computerRepository
            .findById(computer.getId())
            .map(existingComputer -> {
                if (computer.getMacAddress() != null) {
                    existingComputer.setMacAddress(computer.getMacAddress());
                }
                if (computer.getIpAddress() != null) {
                    existingComputer.setIpAddress(computer.getIpAddress());
                }
                if (computer.getEmployeeAbbreviation() != null) {
                    existingComputer.setEmployeeAbbreviation(computer.getEmployeeAbbreviation());
                }
                if (computer.getDescription() != null) {
                    existingComputer.setDescription(computer.getDescription());
                }

                return existingComputer;
            })
            .map(computerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, computer.getId().toString())
        );
    }

    /**
     * {@code GET  /computers} : get all the computers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of computers in body.
     */
    @GetMapping("/computers")
    public ResponseEntity<List<Computer>> getAllComputers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Computers");
        Page<Computer> page = computerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /computers/:id} : get the "id" computer.
     *
     * @param id the id of the computer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the computer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/computers/{id}")
    public ResponseEntity<Computer> getComputer(@PathVariable Long id) {
        log.debug("REST request to get Computer : {}", id);
        Optional<Computer> computer = computerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(computer);
    }

    /**
     * {@code DELETE  /computers/:id} : delete the "id" computer.
     *
     * @param id the id of the computer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/computers/{id}")
    public ResponseEntity<Void> deleteComputer(@PathVariable Long id) {
        log.debug("REST request to delete Computer : {}", id);
        computerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
