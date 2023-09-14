package org.acme.employees.controller.dto;

import jakarta.validation.constraints.Email;

public record EmployeeUpdateRequest(

        String firstname,
        String lastname,

        @Email
        String email
) {
}
