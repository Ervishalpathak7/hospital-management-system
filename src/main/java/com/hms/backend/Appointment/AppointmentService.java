package com.hms.backend.Appointment;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.hms.backend.Appointment.dto.AppointmentCreateDTO;
import com.hms.backend.Exceptions.InvalidTimeSlotException;
import com.hms.backend.Exceptions.ResourceNotFoundException;
import com.hms.backend.Exceptions.SlotAlreadyBookedException;
import com.hms.backend.Patients.PatientRepository;
import com.hms.backend.doctors.DoctorRepository;

@Service
public class AppointmentService {
    private final AppointmentRepository appointMentrepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository,
            PatientRepository patientRepository) {
        this.appointMentrepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment createAppointment(AppointmentCreateDTO data) {
        int minutes = data.startTime().getMinute();
        System.out.println("Minutes :" + minutes);
        if (minutes != 0 && minutes != 30)
            throw new InvalidTimeSlotException("Invalid time slow");

        if (data.startTime().isBefore(LocalTime.of(10, 00)) || data.startTime().isAfter(LocalTime.of(17, 00)))
            throw new InvalidTimeSlotException("Invalid time slot");

        doctorRepository.findById(data.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        patientRepository.findById(data.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (appointMentrepository.existsByDoctorIdAndDateAndStartTime(data.doctorId(), data.date(), data.startTime())) {
            throw new SlotAlreadyBookedException("Desired Slot is already booked");
        }
        Appointment appointment = new Appointment(data);
        return appointMentrepository.save(appointment);
    }
}
