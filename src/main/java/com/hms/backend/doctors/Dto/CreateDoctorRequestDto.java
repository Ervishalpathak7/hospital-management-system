package com.hms.backend.doctors.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.hms.backend.doctors.*;
import com.hms.backend.doctors.types.DoctorSpecialisation;

public record CreateDoctorRequestDto(
                @NotBlank(message = "Inavlid name") @Size(max = 100, message = "Name must be smaller than 100 characters") String name,
                @NotNull(message = "Specialization is required") DoctorSpecialisation specialization) {
}
