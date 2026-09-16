package com.hms.backend.doctors;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.backend.Exceptions.ResourceNotFoundException;
import com.hms.backend.doctors.Dto.CreateDoctorRequest;
import com.hms.backend.doctors.Dto.DoctorResponse;
import com.hms.backend.doctors.Dto.PageResponse;

@Service
public class DoctorServices {

    private final DoctorRepository repository;

    DoctorServices(DoctorRepository repository) {
        this.repository = repository;
    }

    public Doctor getDoctorById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    public PageResponse<DoctorResponse> getAllDoctors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Doctor> doctors = repository.findAll(pageable);
        return PageResponse.from(doctors.map(DoctorResponse::from));
    }

    @Transactional
    public DoctorResponse registerDoctor(CreateDoctorRequest req) {
        Doctor doc = new Doctor(req.name(), req.specialization());
        return DoctorResponse.from(repository.save(doc));
    }

    public void deleteDoctorById(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        repository.deleteById(id);
    }
}
