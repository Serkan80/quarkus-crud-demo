package org.acme.employees.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.acme.employees.controller.dto.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

@Entity
@Table(name = "employee")
public class EmployeeEntity extends PanacheEntity {

    @NotNull
    public UUID externalId;

    @NotBlank
    public String firstname;

    @NotBlank
    public String lastname;

    @NotBlank
    @PastOrPresent
    public LocalDate birthday;

    @Email
    @NotBlank
    public String email;

    @NotBlank
    @Pattern(regexp = "(\\+\\d{2,3})-\\d{7,8}")
    public String cellphone;

    @NotNull
    @Enumerated(STRING)
    public Gender gender;

    public EmployeeEntity() {
    }

    public EmployeeEntity(Employee employee) {
        this.firstname = employee.firstname();
        this.lastname = employee.lastname();
        this.birthday = employee.birthday();
        this.cellphone = employee.cellphone();
        this.email = employee.email();
        this.gender = employee.gender();
    }

    public static Optional<Employee> findById(UUID id) {
        return find("externalId = ?1", id).project(Employee.class).firstResultOptional();
    }

    public static List<Employee> findByGender(Gender gender) {
        return find("gender = ?1", gender)
                .withHint(HINT_READONLY, true)
                .project(Employee.class)
                .list();
    }

    public static List<Employee> listAll() {
        return findAll()
                .withHint(HINT_READONLY, true)
                .project(Employee.class)
                .list();
    }

    public Employee toDto() {
        return new Employee(this.externalId, this.firstname, this.lastname, this.birthday, this.email, this.cellphone, this.gender);
    }
}
