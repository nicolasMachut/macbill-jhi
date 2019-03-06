package fr.nicolasmachut.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A WorkDay.
 */
@Entity
@Table(name = "work_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkDay implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "all_day", nullable = false)
    private Boolean allDay;

    @NotNull
    @Column(name = "draggable", nullable = false)
    private Boolean draggable;

    @NotNull
    @Column(name = "jhi_end", nullable = false)
    private LocalDate end;

    @NotNull
    @Column(name = "jhi_start", nullable = false)
    private LocalDate start;

    @Column(name = "title")
    private LocalDate title;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("workDays")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAllDay() {
        return allDay;
    }

    public WorkDay allDay(Boolean allDay) {
        this.allDay = allDay;
        return this;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean isDraggable() {
        return draggable;
    }

    public WorkDay draggable(Boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public LocalDate getEnd() {
        return end;
    }

    public WorkDay end(LocalDate end) {
        this.end = end;
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public WorkDay start(LocalDate start) {
        this.start = start;
        return this;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getTitle() {
        return title;
    }

    public WorkDay title(LocalDate title) {
        this.title = title;
        return this;
    }

    public void setTitle(LocalDate title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public WorkDay user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public WorkDay customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkDay workDay = (WorkDay) o;
        if (workDay.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workDay.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkDay{" +
            "id=" + getId() +
            ", allDay='" + isAllDay() + "'" +
            ", draggable='" + isDraggable() + "'" +
            ", end='" + getEnd() + "'" +
            ", start='" + getStart() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
