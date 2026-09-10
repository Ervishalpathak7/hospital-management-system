package com.hms.backend.doctors.Dto;

import java.util.UUID;

import com.hms.backend.doctors.Doctor;
import com.hms.backend.doctors.DoctorSpecialisation;

public record DoctorResponse(
        UUID id,
        String name,
        DoctorSpecialisation specialization) {
    public static DoctorResponse from(Doctor d) {
        return new DoctorResponse(d.getId(), d.getName(), d.getSpecialization());
    }
}
