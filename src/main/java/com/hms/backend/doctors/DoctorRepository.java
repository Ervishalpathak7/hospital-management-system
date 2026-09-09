package com.hms.backend.doctors;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    <S extends Doctor> S save(S entity);

    List<Doctor> findAll();

    Optional<Doctor> findById(Long id);

    void deleteById(Long id);

}
