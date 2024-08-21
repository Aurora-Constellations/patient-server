CREATE DATABASE patienttracker;
\c patienttracker;

CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    unit_number TEXT NOT NULL,
    last_name TEXT NOT NULL,
    first_name TEXT NOT NULL,
    sex TEXT NOT NULL,
    dob DATE NOT NULL,
    hcn TEXT,
    family TEXT,
    fam_priv TEXT,
    hosp TEXT,
    flag TEXT,
    address1 TEXT,
    address2 TEXT,
    city TEXT,
    province TEXT,
    postal_code TEXT,
    home_phone_number TEXT,
    work_phone_number TEXT,
    ohip TEXT,
    family_physician TEXT,
    attending TEXT,
    collab1 TEXT,
    collab2 TEXT
);

CREATE TABLE IF NOT EXISTS reports (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT REFERENCES patients(id) ON DELETE CASCADE,
    unit_number TEXT NOT NULL,
    systolic_pressure TEXT NOT NULL,
    diastolic_pressure TEXT NOT NULL,
    has_hypertension BOOLEAN,
    glucose_level TEXT NOT NULL,
    glycated_hemoglobin TEXT NOT NULL,
    has_diabetes BOOLEAN
);