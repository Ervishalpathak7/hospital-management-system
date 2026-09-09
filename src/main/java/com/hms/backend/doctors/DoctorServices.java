package com.hms.backend.doctors;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.hms.backend.Exceptions.ResourceNotFound;

@Service
public class DoctorServices {

    private final DoctorRepository repository;

    DoctorServices(DoctorRepository repository) {
        this.repository = repository;
    }

    public Optional<Doctor> getDoctorById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.out.println("Error occured inside { getDoctorById Service }  : " + e.getMessage());
            return null;
        }
    }

    public List<Doctor> getAllDoctors() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            System.out.println("Error occured inside { getAllDoctors Service }  : " + e.getMessage());
            return null;
        }
    }

    public Doctor registerDoctor(Doctor doctor) {
        try {
            return repository.save(doctor);
        } catch (Exception e) {
            System.out.println("Error occured inside { registerDoctor Service }  : " + e.getMessage());
            return null;
        }
    }

    public void deleteDoctorById(Long id) {
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
