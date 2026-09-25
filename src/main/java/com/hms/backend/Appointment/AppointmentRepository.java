package com.hms.backend.Appointment;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByDoctorIdAndDateAndStartTime(UUID doctorId, LocalDate date, LocalTime startTime);
}
