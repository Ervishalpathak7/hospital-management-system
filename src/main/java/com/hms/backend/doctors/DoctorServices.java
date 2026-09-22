package com.hms.backend.doctors;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Limit;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.backend.Exceptions.ResourceNotFoundException;
import com.hms.backend.Types.CursorPageResponseDto;
import com.hms.backend.Utils.CursorCodec;
import com.hms.backend.doctors.dto.CreateDoctorRequestDto;
import com.hms.backend.doctors.dto.DoctorByIdResponse;
import com.hms.backend.doctors.dto.DoctorResponseDto;
import com.hms.backend.doctors.dto.UpdateDoctorRequstDto;

@Service
public class DoctorServices {

    private final DoctorRepository repository;

    DoctorServices(DoctorRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public DoctorByIdResponse getDoctorById(UUID id) {
        return DoctorByIdResponse
                .from(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found")));
    }

    @Transactional(readOnly = true)
    public CursorPageResponseDto<DoctorResponseDto> getAllDoctors(String cursor, int size) {

        Limit limit = Limit.of(size + 1);

        List<Doctor> rows = (cursor == null || cursor.isBlank())
                ? repository.findAllByOrderByIdAsc(limit)
                : repository.findByIdGreaterThanOrderByIdAsc(CursorCodec.decode(cursor), limit);

        Boolean hasNext = rows.size() > size;

        List<Doctor> pageRows = hasNext ? rows.subList(0, size) : rows;

        String nextCursor = hasNext ? CursorCodec.encode(pageRows.get(pageRows.size() - 1).getId()) : null;

        List<DoctorResponseDto> content = pageRows.stream()
                .map(DoctorResponseDto::from)
                .toList();

        return new CursorPageResponseDto<>(content, nextCursor, hasNext);
    }

    @Transactional
    public DoctorResponseDto registerDoctor(CreateDoctorRequestDto req) {
        Doctor doc = new Doctor(req.name().trim(), req.specialization());
        return DoctorResponseDto.from(repository.save(doc));
    }

    @Transactional
    public DoctorResponseDto updateDoctorById(UUID id, UpdateDoctorRequstDto req) {
        Doctor doc = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        if (!doc.getVersion().equals(req.version())) {
            throw new ObjectOptimisticLockingFailureException(Doctor.class, id);
        }
        if (req.name() != null) {
            doc.changeName(req.name());
        }
        if (req.specialization() != null) {
            doc.changeSpecialization(req.specialization());
        }
        return DoctorResponseDto.from(doc);

    }

    @Transactional
    public void deleteDoctorById(UUID id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        repository.deleteById(id);
    }
}
