package com.hms.backend.doctors.Dto;

import java.util.UUID;

import com.hms.backend.doctors.Doctor;

public record DoctorResponse(
        UUID id,
        String name,
        String specialization) {
    public static DoctorResponse from(Doctor d) {
        return new DoctorResponse(d.getId(), d.getName(), d.getSpeciality());
    }
}
