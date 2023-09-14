package org.acme.employees.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.acme.employees.controller.dto.Employee;
import org.acme.employees.controller.dto.EmployeeUpdateRequest;
import org.acme.employees.model.EmployeeEntity;

import java.util.UUID;

import static io.quarkus.runtime.util.StringUtil.isNullOrEmpty;

@ApplicationScoped
public class EmployeeService {

    @Transactional
    public Employee update(UUID id, EmployeeUpdateRequest request) {
        var employee = EmployeeEntity
                .find("externalId = ?1", id)
                .<EmployeeEntity>firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Employee(extId=%s) not found".formatted(id)));

        if (!isNullOrEmpty(request.firstname())) {
            employee.firstname = request.firstname();
        }

        if (!isNullOrEmpty(request.lastname())) {
            employee.lastname = request.lastname();
        }

        if (!isNullOrEmpty(request.email())) {
            employee.email = request.email();
        }

        Log.infof("Employee(extId=%s) updated", id);
        return employee.toDto();
    }

    @Transactional
    public Employee save(Employee employee) {
        // this way of mapping is just for the sake of demo, for production use something like MapStruct
        var entity = new EmployeeEntity(employee);
        entity.externalId = UUID.randomUUID();
        entity.persist();

        Log.infof("Employee(extId=%s) created", entity.externalId);
        return entity.toDto();
    }

    @Transactional
    public void deleteById(UUID id) {
        var count = EmployeeEntity.delete("externalId = ?1", id);
        if (count > 0) {
            Log.infof("Employee(extId=%s) deleted", id);
        }
    }
}
