package org.acme.employees.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.acme.employees.controller.dto.Employee;
import org.acme.employees.controller.dto.EmployeeUpdateRequest;
import org.acme.employees.model.EmployeeEntity;
import org.acme.employees.model.Gender;
import org.acme.employees.service.EmployeeService;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/employees")
public class EmployeeController {

    @Inject
    EmployeeService employeeService;

    @POST
    public RestResponse<Void> save(Employee employee) {
        var result = this.employeeService.save(employee);
        return RestResponse.created(URI.create("/employees/" + result.externalId()));
    }

    @PATCH
    @Path("/{id}")
    public RestResponse<Employee> update(UUID id, EmployeeUpdateRequest request) {
        return RestResponse.ok(this.employeeService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    public RestResponse<Void> delete(UUID id) {
        this.employeeService.deleteById(id);
        return RestResponse.noContent();
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
