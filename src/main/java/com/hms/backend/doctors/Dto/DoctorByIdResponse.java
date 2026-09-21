package com.hms.backend.doctors.dto;

import java.util.UUID;

import com.hms.backend.doctors.Doctor;
import com.hms.backend.doctors.types.DoctorSpecialisation;

public record DoctorByIdResponse(
        UUID id,
        String name,
        DoctorSpecialisation specialization,
        Long version) {
    public static DoctorByIdResponse from(Doctor doc) {
        return new DoctorByIdResponse(doc.getId(), doc.getName(), doc.getSpecialization(), doc.getVersion());
    }

}
