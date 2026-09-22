package com.hms.backend.Patients.dto;

import java.time.LocalDate;

import com.hms.backend.Patients.Patient;
import com.hms.backend.Types.Gender;

public record ResponsePatient(
        String name,
        LocalDate dob,
        Gender gender,
        String phone,
        String email

) {
    public static ResponsePatient from(Patient p) {
        return new ResponsePatient(p.getName(), p.getDob(), p.getGender(), p.getPhone(), p.getEmail());
    }

}
