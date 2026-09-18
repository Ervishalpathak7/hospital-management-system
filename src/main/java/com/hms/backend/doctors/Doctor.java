package com.hms.backend.doctors;

import java.util.UUID;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)

    @Column(name = "specialization", columnDefinition = "doc_specialization", nullable = false)
    private DoctorSpecialisation specialization;

    public Doctor(String name, DoctorSpecialisation specialization) {
        this.name = name.trim();
        this.specialization = specialization;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeSpecialization(DoctorSpecialisation specialisation) {
        this.specialization = specialisation;
    }

}
