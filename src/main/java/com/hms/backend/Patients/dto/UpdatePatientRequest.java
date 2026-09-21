package com.hms.backend.Patients.dto;

import java.time.LocalDate;

import com.hms.backend.Types.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePatientRequest(

                @NotNull(message = "Version is required") Long version,

                @Size(max = 255) @Pattern(regexp = ".*\\S.*", message = "Name must not be blank") String name,

                Gender gender,

                @Past(message = "Date of birth must be in the past") LocalDate dob,

                @Size(max = 15) @Pattern(regexp = "^[+]?[0-9]{7,15}$", message = "Invalid phone number") String phone,

                @Email(message = "Invalid email address") @Size(max = 100) String email) {
}