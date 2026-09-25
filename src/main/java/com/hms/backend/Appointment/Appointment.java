package com.hms.backend.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hms.backend.Appointment.dto.AppointmentCreateDTO;
import com.hms.backend.Types.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;

    @Column(name = "doctor_id", nullable = false, updatable = false)
    UUID doctorId;

    @Column(name = "patient_id", nullable = false)
    UUID patientId;

    @Column(name = "appointment_date", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate date;

    @Column(name = "start_time", nullable = false, updatable = false)
    LocalTime startTime;

    @Column(name = "end_time", nullable = false, updatable = false)
    LocalTime endTime;

    @Column(name = "status", nullable = true, updatable = false)
    String status;

    @Column(name = "notes", nullable = true, updatable = true)
    String notes;

    @Version
    @Column(name = "version", nullable = false)
    Long version;

    public Appointment(AppointmentCreateDTO data) {
        this.patientId = data.patientId();
        this.doctorId = data.doctorId();
        this.date = data.date();
        this.startTime = data.startTime();
        this.endTime = data.startTime().plusMinutes(30);
        this.status = "SCHEDULED";
    }
}
