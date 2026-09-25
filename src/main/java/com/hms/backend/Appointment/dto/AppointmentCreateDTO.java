package com.hms.backend.Appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record AppointmentCreateDTO(
        @NotBlank UUID doctorId,
        @NotBlank UUID patientId,
        @NotNull @Past(message = "Appointment Date should not be in past") @JsonFormat(pattern = "dd-MM-yy") LocalDate date,
        @NotNull LocalTime startTime) {
}
