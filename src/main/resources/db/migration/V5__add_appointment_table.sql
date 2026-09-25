CREATE TABLE appointments (
    id               UUID         PRIMARY KEY,
    doctor_id        UUID         NOT NULL REFERENCES doctors(id),
    patient_id       UUID         NOT NULL REFERENCES patients(id),
    appointment_date DATE         NOT NULL,
    start_time       TIME         NOT NULL,
    end_time         TIME         NOT NULL,
    status           VARCHAR(20)  NOT NULL
                     CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    notes            TEXT,
    version          BIGINT       NOT NULL DEFAULT 0,
    CHECK (end_time > start_time)
);

-- a doctor can't have two active appointments in the same slot
CREATE UNIQUE INDEX uk_doctor_active_slot
    ON appointments (doctor_id, appointment_date, start_time)
    WHERE status <> 'CANCELLED';

-- a patient can't be in two places at once either
CREATE UNIQUE INDEX uk_patient_active_slot
    ON appointments (patient_id, appointment_date, start_time)
    WHERE status <> 'CANCELLED';