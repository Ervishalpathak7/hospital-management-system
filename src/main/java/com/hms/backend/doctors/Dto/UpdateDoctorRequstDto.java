package com.hms.backend.doctors.dto;

import com.hms.backend.doctors.types.DoctorSpecialisation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateDoctorRequstDto(
                @Size(max = 100, message = "Name must be at most 100 character") @Pattern(regexp = ".*\\S.*", message = "Name must not be blank") String name,
                @NotNull(message = "Version is required") Long version,
                DoctorSpecialisation specialization) {
}
