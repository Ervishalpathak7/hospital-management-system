package com.hms.backend.doctors;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.backend.Exceptions.ResourceNotFound;
import com.hms.backend.doctors.Dto.CreateDoctorRequest;

@Service
public class DoctorServices {

    private final DoctorRepository repository;

    DoctorServices(DoctorRepository repository) {
        this.repository = repository;
    }

    public Doctor getDoctorById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
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
        try {

            Optional<Doctor> doc = repository.findById(id);
            if (doc.isPresent()) {
                repository.deleteById(id);
            } else {
                throw new ResourceNotFound("doctor with id : " + id + " not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
