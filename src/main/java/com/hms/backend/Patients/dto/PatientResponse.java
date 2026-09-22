package com.hms.backend.Patients.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.hms.backend.Patients.Patient;
import com.hms.backend.Types.Gender;

public record PatientResponse(
        UUID id,
        String name,
        LocalDate dob,
        Gender gender,
        String phone,
        String email) {
    public static PatientResponse from(Patient p) {
        return new PatientResponse(p.getId(), p.getName(), p.getDob(), p.getGender(), p.getPhone(), p.getEmail());
    }

}
