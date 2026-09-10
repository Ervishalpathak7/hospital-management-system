
TRUNCATE TABLE doctors;

CREATE TYPE doc_specialization AS ENUM (
    'General Medicine', 
    'Paediatrics', 
    'Gynaecology', 
    'Cardiology', 
    'Orthopedics', 
    'Dermatology', 
    'Neurology', 
    'General Surgery', 
    'Radiology', 
    'Psychiatry'
);



ALTER TABLE doctors DROP COLUMN speciality;
ALTER TABLE doctors ADD COLUMN specialization doc_specialization NOT NULL DEFAULT 'General Medicine';