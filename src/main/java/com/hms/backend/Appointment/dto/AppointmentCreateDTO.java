package com.hms.backend.Appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record AppointmentCreateDTO(
        @NotNull UUID doctorId,
        @NotNull UUID patientId,
        @NotNull @FutureOrPresent  (message = "Appointment Date should not be in past") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
        @NotNull LocalTime startTime) {
}
