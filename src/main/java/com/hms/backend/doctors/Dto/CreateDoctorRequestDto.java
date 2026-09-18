package com.hms.backend.doctors.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.hms.backend.doctors.*;

public record CreateDoctorRequestDto(
        @NotBlank(message = "inavlid name") @Size(max = 100) String name,
        @NotNull(message = "Specialization is required") DoctorSpecialisation specialization) {
}
