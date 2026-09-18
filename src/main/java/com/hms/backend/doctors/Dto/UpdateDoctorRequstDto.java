package com.hms.backend.doctors.Dto;

import com.hms.backend.doctors.DoctorSpecialisation;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateDoctorRequstDto(
        @Size(max = 100, message = "Name must be at most 100 character") @Pattern(regexp = ".*\\S.*", message = "Name must not be blank") String name,
        DoctorSpecialisation specialization) {
}
