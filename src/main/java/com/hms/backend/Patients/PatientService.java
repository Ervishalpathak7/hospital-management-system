package com.hms.backend.Patients;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.backend.Exceptions.ResourceNotFoundException;
import com.hms.backend.Patients.dto.CreatePatientRequest;
import com.hms.backend.Patients.dto.PatientResponse;
import com.hms.backend.Types.CursorPageResponseDto;
import com.hms.backend.Utils.CursorCodec;

@Service
public class PatientService {
    private final PatientRepository repository;

    PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Patient createPatient(CreatePatientRequest req) {
        Patient p = new Patient(req.name(), req.dob(), req.gender(), req.phone(), req.email());
        return repository.save(p);
    }

    @Transactional(readOnly = true)
    public Patient getPatientById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    @Transactional(readOnly = true)
    public CursorPageResponseDto<PatientResponse> getAllPatient(int size, String cursor) {
        Limit limit = Limit.of(size + 1);

        List<Patient> patientRows = (cursor == null || cursor.isBlank())
                ? repository.findAllByOrderByIdAsc(limit)
                : repository.findByIdGreaterThanOrderByIdAsc(CursorCodec.decode(cursor), limit);

        Boolean hasNext = patientRows.size() > size;
        List<Patient> patientList = hasNext ? patientRows.subList(0, size) : patientRows;
        String nextCursor = hasNext ? CursorCodec.encode(patientList.get(patientList.size() - 1).getId()) : null;

        List<PatientResponse> patients = patientList.stream()
                .map(PatientResponse::from)
                .toList();

        return new CursorPageResponseDto<>(patients, nextCursor, hasNext);
    }

    @Transactional
    public void deletePatientById(UUID id) {
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        repository.deleteById(id);
    }
}
