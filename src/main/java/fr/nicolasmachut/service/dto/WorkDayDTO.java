package fr.nicolasmachut.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the WorkDay entity.
 */
public class WorkDayDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean allDay;

    @NotNull
    private Boolean draggable;

    @NotNull
    private LocalDate end;

    @NotNull
    private LocalDate start;

    private LocalDate title;


    private Long userId;

    private String userLogin;

    private Long customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getTitle() {
        return title;
    }

    public void setTitle(LocalDate title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkDayDTO workDayDTO = (WorkDayDTO) o;
        if (workDayDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workDayDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkDayDTO{" +
            "id=" + getId() +
            ", allDay='" + isAllDay() + "'" +
            ", draggable='" + isDraggable() + "'" +
            ", end='" + getEnd() + "'" +
            ", start='" + getStart() + "'" +
            ", title='" + getTitle() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", customer=" + getCustomerId() +
            "}";
    }
}
