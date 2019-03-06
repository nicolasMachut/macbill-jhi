package fr.nicolasmachut.service.impl;

import fr.nicolasmachut.service.WorkDayService;
import fr.nicolasmachut.domain.WorkDay;
import fr.nicolasmachut.repository.WorkDayRepository;
import fr.nicolasmachut.service.dto.WorkDayDTO;
import fr.nicolasmachut.service.mapper.WorkDayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing WorkDay.
 */
@Service
@Transactional
public class WorkDayServiceImpl implements WorkDayService {

    private final Logger log = LoggerFactory.getLogger(WorkDayServiceImpl.class);

    private final WorkDayRepository workDayRepository;

    private final WorkDayMapper workDayMapper;

    public WorkDayServiceImpl(WorkDayRepository workDayRepository, WorkDayMapper workDayMapper) {
        this.workDayRepository = workDayRepository;
        this.workDayMapper = workDayMapper;
    }

    /**
     * Save a workDay.
     *
     * @param workDayDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkDayDTO save(WorkDayDTO workDayDTO) {
        log.debug("Request to save WorkDay : {}", workDayDTO);
        WorkDay workDay = workDayMapper.toEntity(workDayDTO);
        workDay = workDayRepository.save(workDay);
        return workDayMapper.toDto(workDay);
    }

    /**
     * Get all the workDays.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkDayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkDays");
        return workDayRepository.findAll(pageable)
            .map(workDayMapper::toDto);
    }


    /**
     * Get one workDay by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkDayDTO> findOne(Long id) {
        log.debug("Request to get WorkDay : {}", id);
        return workDayRepository.findById(id)
            .map(workDayMapper::toDto);
    }

    /**
     * Delete the workDay by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkDay : {}", id);        workDayRepository.deleteById(id);
    }
}
