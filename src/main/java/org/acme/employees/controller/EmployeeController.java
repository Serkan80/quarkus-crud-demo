package org.acme.employees.controller;

import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.acme.employees.controller.dto.Employee;
import org.acme.employees.model.EmployeeEntity;
import org.acme.employees.model.Gender;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/employees")
public class EmployeeController {

    @POST
    @Transactional
    public RestResponse<Void> save(Employee employee) {
        // this way of mapping is just for the sake of demo, for production use something like MapStruct
        var entity = new EmployeeEntity(employee);
        entity.externalId = UUID.randomUUID();
        entity.persist(entity);

        Log.infof("Employee(extId=%s) created", entity.externalId);
        return RestResponse.created(URI.create("/employees/" + entity.externalId));
    }

    @GET
    public RestResponse<List<Employee>> findAll() {
        return RestResponse.ok(EmployeeEntity.listAll());
    }

    @GET
    @Path("/{id}")
    public RestResponse<Employee> findById(UUID id) {
        var result = EmployeeEntity.findById(id).orElseThrow(() -> new NotFoundException("Employee(extId=%s) not found".formatted(id)));
        return RestResponse.ok(result);
    }

    @GET
    @Path("/gender/{gender}")
    public RestResponse<List<Employee>> findByGender(Gender gender) {
        var result = EmployeeEntity.findByGender(gender);
        return RestResponse.ok(result);
    }
}
