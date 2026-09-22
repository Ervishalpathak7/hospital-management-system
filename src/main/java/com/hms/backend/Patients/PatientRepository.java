package com.hms.backend.Patients;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    List<Patient> findAllByOrderByIdAsc(Limit limit);
    List<Patient> findByIdGreaterThanOrderByIdAsc(UUID id, Limit limit);
}
