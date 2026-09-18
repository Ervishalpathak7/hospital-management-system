package com.hms.backend.doctors.Dto;

import java.util.UUID;

import com.hms.backend.doctors.Doctor;
import com.hms.backend.doctors.DoctorSpecialisation;

public record DoctorResponseDto(
        UUID id,
        String name,
        DoctorSpecialisation specialization) {
    public static DoctorResponseDto from(Doctor d) {
        return new DoctorResponseDto(d.getId(), d.getName(), d.getSpecialization());
    }
}
