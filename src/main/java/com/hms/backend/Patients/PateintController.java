package com.hms.backend.Patients;

import org.springframework.web.bind.annotation.RestController;

import com.hms.backend.Patients.dto.CreatePatientRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/patient")
public class PateintController {
    private final PatientService service;

    PateintController(PatientService service) {
        this.service = service;
    }

    @PostMapping
    public PatientResponse createPatient(@Valid @RequestBody CreatePatientRequest data) {
        return service.createPatient(data);
    }

}
