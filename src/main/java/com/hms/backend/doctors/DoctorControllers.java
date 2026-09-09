package com.hms.backend.doctors;

import org.springframework.web.bind.annotation.RestController;

import com.hms.backend.doctors.Dto.CreateDoctorRequest;
import com.hms.backend.doctors.Dto.DoctorResponse;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorControllers {

    private final DoctorServices service;

    DoctorControllers(DoctorServices service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Doctor> getAllDoctors() {
        return service.getAllDoctors();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse registerNewDoctor(@Valid @RequestBody CreateDoctorRequest data) {
        Doctor doc = service.registerDoctor(data);
        return DoctorResponse.from(doc);
    }

    @GetMapping("/{id}")
    public Optional<Doctor> getDoctorById(@PathVariable String id) {
        return service.getDoctorById(UUID.fromString(id));
    }

    @DeleteMapping("/{id}")
    public String deleteDoctorById(@PathVariable String id) {
        service.deleteDoctorById(UUID.fromString(id));
        return "Doctor deleted successfully";
    }

}
