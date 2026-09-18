package com.hms.backend.doctors;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.backend.Exceptions.ResourceNotFoundException;
import com.hms.backend.doctors.Dto.CreateDoctorRequest;
import com.hms.backend.doctors.Dto.CursorPageResponse;
import com.hms.backend.doctors.Dto.DoctorResponse;

@Service
public class DoctorServices {

    private final DoctorRepository repository;

    DoctorServices(DoctorRepository repository) {
        this.repository = repository;
    }

    public Doctor getDoctorById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Transactional(readOnly = true)
    public CursorPageResponse<DoctorResponse> getAllDoctors(String cursor, int size) {

        Limit limit = Limit.of(size + 1);

        List<Doctor> rows = (cursor == null || cursor.isBlank())
                ? repository.findAllByOrderByIdAsc(limit)
                : repository.findByIdGreaterThanOrderByIdAsc(CursorCodec.decode(cursor), limit);

        Boolean hasNext = rows.size() > size;

        List<Doctor> pageRows = hasNext ? rows.subList(0, size) : rows;

        String nextCursor = hasNext ? CursorCodec.encode(pageRows.get(pageRows.size() - 1).getId()) : null;

        List<DoctorResponse> content = pageRows.stream()
                .map(DoctorResponse::from)
                .toList();

        return new CursorPageResponse<>(content, nextCursor, hasNext);
    }

    @Transactional
    public DoctorResponse registerDoctor(CreateDoctorRequest req) {
        Doctor doc = new Doctor(req.name(), req.specialization());
        return DoctorResponse.from(repository.save(doc));
    }

    public void deleteDoctorById(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        repository.deleteById(id);
    }
}
