package fr.nicolasmachut.repository;

import fr.nicolasmachut.domain.WorkDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the WorkDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {

    @Query("select work_day from WorkDay work_day where work_day.user.login = ?#{principal.username}")
    List<WorkDay> findByUserIsCurrentUser();

}
