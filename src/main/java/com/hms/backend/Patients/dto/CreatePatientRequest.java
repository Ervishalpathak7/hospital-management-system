package com.hms.backend.Patients.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hms.backend.Types.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePatientRequest(

        @NotBlank(message = "Name is required") @Size(max = 255, message = "Name must be at most 255 characters") String name,

        @NotNull(message = "Gender is required") Gender gender,

        @NotNull(message = "Date of birth is required") @Past(message = "Date of birth must be in the past") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dob,

        @NotBlank(message = "Phone is required") @Size(max = 15, message = "Phone must be at most 15 characters") @Pattern(regexp = "^[+]?[0-9]{7,15}$", message = "Invalid phone number") String phone,

        @Email(message = "Invalid email address") @Size(max = 100) String email) {
}