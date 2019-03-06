package fr.nicolasmachut.service;

import fr.nicolasmachut.service.dto.WorkDayDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing WorkDay.
 */
public interface WorkDayService {

    /**
     * Save a workDay.
     *
     * @param workDayDTO the entity to save
     * @return the persisted entity
     */
    WorkDayDTO save(WorkDayDTO workDayDTO);

    /**
     * Get all the workDays.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WorkDayDTO> findAll(Pageable pageable);


    /**
     * Get the "id" workDay.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WorkDayDTO> findOne(Long id);

    /**
     * Delete the "id" workDay.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
