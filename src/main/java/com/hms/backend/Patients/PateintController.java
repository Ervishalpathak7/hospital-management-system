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

    @GetMapping
    public CursorPageResponseDto<PatientResponse> getAllPatient(
            @RequestParam(defaultValue = "10") @Min(value = 5, message = "Size must be atleast 5") @Max(value = 20, message = "Size must be atmost 20") int size,
            @RequestParam(required = false) String cursor) {
        return service.getAllPatient(size, cursor);
    }

    @PostMapping
    public PatientResponse createPatient(@Valid @RequestBody CreatePatientRequest data) {
        return service.createPatient(data);
    }

}
