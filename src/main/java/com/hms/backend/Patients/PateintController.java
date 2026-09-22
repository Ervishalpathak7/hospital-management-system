package com.hms.backend.Patients;

import org.springframework.web.bind.annotation.RestController;

import com.hms.backend.Patients.dto.CreatePatientRequest;
import com.hms.backend.Patients.dto.PatientResponse;
import com.hms.backend.Types.CursorPageResponseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @GetMapping("{id}")
    public PatientResponse getPatientById(@RequestParam UUID id) {
        return PatientResponse.from(service.getPatientById(id));
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatientById(@NotNull @PathVariable UUID id) {
        service.deletePatientById(id);
    }

}
