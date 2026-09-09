package com.hms.backend.doctors;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    <S extends Doctor> S save(S entity);

    List<Doctor> findAll();

    Optional<Doctor> findById(UUID id);

    void deleteById(UUID id);

}
