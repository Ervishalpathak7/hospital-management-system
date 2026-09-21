

CREATE TYPE Gender AS ENUM (
    'Male',
    'Female',
    'Other'
);

CREATE TABLE patients(
    id          UUID            PRIMARY KEY DEFAULT     gen_random_uuid(),
    name        VARCHAR(255)    NOT NULL,
    gender      Gender          NOT NULL,
    dob         DATE            NOT NULL,
    phone       VARCHAR(15)     NOT NULL,
    email       VARCHAR(100),   
    version     BIGINT          DEFAULT 0
);