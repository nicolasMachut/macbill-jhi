package fr.nicolasmachut.service.mapper;

import fr.nicolasmachut.domain.*;
import fr.nicolasmachut.service.dto.WorkDayDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WorkDay and its DTO WorkDayDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CustomerMapper.class})
public interface WorkDayMapper extends EntityMapper<WorkDayDTO, WorkDay> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "customer.id", target = "customerId")
    WorkDayDTO toDto(WorkDay workDay);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "customerId", target = "customer")
    WorkDay toEntity(WorkDayDTO workDayDTO);

    default WorkDay fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkDay workDay = new WorkDay();
        workDay.setId(id);
        return workDay;
    }
}
