package org.acme.employees.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.acme.employees.model.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record Employee(

        UUID externalId,

        @NotBlank
        String firstname,

        @NotBlank
        String lastname,

        @NotNull
        @PastOrPresent
        LocalDate birthday,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp = "(\\+\\d{2,3})-\\d{7,8}")
        String cellphone,

        @NotNull
        Gender gender
) {
}
