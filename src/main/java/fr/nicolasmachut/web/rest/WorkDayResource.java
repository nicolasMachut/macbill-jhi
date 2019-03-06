package fr.nicolasmachut.web.rest;
import fr.nicolasmachut.service.WorkDayService;
import fr.nicolasmachut.web.rest.errors.BadRequestAlertException;
import fr.nicolasmachut.web.rest.util.HeaderUtil;
import fr.nicolasmachut.web.rest.util.PaginationUtil;
import fr.nicolasmachut.service.dto.WorkDayDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkDay.
 */
@RestController
@RequestMapping("/api")
public class WorkDayResource {

    private final Logger log = LoggerFactory.getLogger(WorkDayResource.class);

    private static final String ENTITY_NAME = "workDay";

    private final WorkDayService workDayService;

    public WorkDayResource(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    /**
     * POST  /work-days : Create a new workDay.
     *
     * @param workDayDTO the workDayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workDayDTO, or with status 400 (Bad Request) if the workDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-days")
    public ResponseEntity<WorkDayDTO> createWorkDay(@Valid @RequestBody WorkDayDTO workDayDTO) throws URISyntaxException {
        log.debug("REST request to save WorkDay : {}", workDayDTO);
        if (workDayDTO.getId() != null) {
            throw new BadRequestAlertException("A new workDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkDayDTO result = workDayService.save(workDayDTO);
        return ResponseEntity.created(new URI("/api/work-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-days : Updates an existing workDay.
     *
     * @param workDayDTO the workDayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workDayDTO,
     * or with status 400 (Bad Request) if the workDayDTO is not valid,
     * or with status 500 (Internal Server Error) if the workDayDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-days")
    public ResponseEntity<WorkDayDTO> updateWorkDay(@Valid @RequestBody WorkDayDTO workDayDTO) throws URISyntaxException {
        log.debug("REST request to update WorkDay : {}", workDayDTO);
        if (workDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkDayDTO result = workDayService.save(workDayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-days : get all the workDays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workDays in body
     */
    @GetMapping("/work-days")
    public ResponseEntity<List<WorkDayDTO>> getAllWorkDays(Pageable pageable) {
        log.debug("REST request to get a page of WorkDays");
        Page<WorkDayDTO> page = workDayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-days");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /work-days/:id : get the "id" workDay.
     *
     * @param id the id of the workDayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workDayDTO, or with status 404 (Not Found)
     */
    @GetMapping("/work-days/{id}")
    public ResponseEntity<WorkDayDTO> getWorkDay(@PathVariable Long id) {
        log.debug("REST request to get WorkDay : {}", id);
        Optional<WorkDayDTO> workDayDTO = workDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workDayDTO);
    }

    /**
     * DELETE  /work-days/:id : delete the "id" workDay.
     *
     * @param id the id of the workDayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-days/{id}")
    public ResponseEntity<Void> deleteWorkDay(@PathVariable Long id) {
        log.debug("REST request to delete WorkDay : {}", id);
        workDayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
