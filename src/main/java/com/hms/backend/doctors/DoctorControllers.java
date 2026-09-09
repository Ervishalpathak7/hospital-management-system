package com.hms.backend.doctors;

import org.springframework.web.bind.annotation.RestController;

import com.hms.backend.doctors.Dto.CreateDoctorRequest;
import com.hms.backend.doctors.Dto.DoctorResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/doctor")
@Validated
public class DoctorControllers {

    private final DoctorServices service;

    DoctorControllers(DoctorServices service) {
        this.service = service;
    }

    @GetMapping()
    public List<Doctor> getAllDoctors(@RequestParam @Min(1) @Max(20) int size) {
        return service.getAllDoctors();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse registerNewDoctor(@Valid @RequestBody CreateDoctorRequest data) {
        Doctor doc = service.registerDoctor(data);
        return DoctorResponse.from(doc);
    }

    @GetMapping("/{id}")
    public Optional<Doctor> getDoctorById(@PathVariable @NotNull UUID id) {
        return service.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDoctorById(@PathVariable @NotNull UUID id) {
        service.deleteDoctorById(id);
        return "Doctor deleted successfully";
    }

}
