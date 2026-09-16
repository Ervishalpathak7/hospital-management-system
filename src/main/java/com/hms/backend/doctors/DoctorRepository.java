package com.hms.backend.doctors;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    List<Doctor> findAllByOrderByIdAsc(Limit limit);
    List<Doctor>findByIdGreaterThanOrderByIdAsc(Long id , Limit limit);

}
