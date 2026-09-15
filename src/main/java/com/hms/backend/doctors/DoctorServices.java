package com.hms.backend.doctors;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.backend.Exceptions.ResourceNotFoundException;
import com.hms.backend.doctors.Dto.CreateDoctorRequest;

@Service
public class DoctorServices {

    private final DoctorRepository repository;

    DoctorServices(DoctorRepository repository) {
        this.repository = repository;
    }

    public Doctor getDoctorById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    public List<Doctor> getAllDoctors() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            System.out.println("Error occured inside { getAllDoctors Service }  : " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public Doctor registerDoctor(CreateDoctorRequest req) {
        try {
            Doctor doc = new Doctor(req.name(), req.specialization());
            return repository.save(doc);
        } catch (Exception e) {
            System.out.println("Error occured inside { registerDoctor Service }  : " + e.getMessage());
            return null;
        }
    }

    public void deleteDoctorById(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        repository.deleteById(id);
    }
}
