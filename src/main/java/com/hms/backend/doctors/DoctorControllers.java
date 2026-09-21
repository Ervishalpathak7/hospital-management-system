package com.hms.backend.doctors;

import com.hms.backend.doctors.dto.DoctorResponseDto;
import com.hms.backend.doctors.dto.CursorPageResponseDto;
import com.hms.backend.doctors.dto.UpdateDoctorRequstDto;
import com.hms.backend.doctors.dto.CreateDoctorRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorControllers {

    private final DoctorServices service;

    DoctorControllers(DoctorServices service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorPageResponseDto<DoctorResponseDto> getAllDoctors(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int size) {
        return service.getAllDoctors(cursor, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDto registerNewDoctor(@Valid @RequestBody CreateDoctorRequestDto data) {
        return service.registerDoctor(data);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorResponseDto getDoctorById(@PathVariable @NotNull UUID id) {
        return service.getDoctorById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorResponseDto updateDoctorById(@PathVariable UUID id, @Valid @RequestBody UpdateDoctorRequstDto entity) {
        return service.updateDoctorById(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable @NotNull UUID id) {
        service.deleteDoctorById(id);
        return ResponseEntity.ok("Doctor Deleted Successfully");
    }

}
