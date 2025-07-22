CREATE DATABASE patienttracker_test;
\c patienttracker_test;

CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    account_number TEXT NOT NULL,
    unit_number TEXT UNIQUE NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    sex TEXT NOT NULL,
    dob DATE,
    hcn TEXT,
    admit_date TIMESTAMP,
    floor TEXT,
    room TEXT,
    bed TEXT,
    mrp TEXT,
    admitting_phys TEXT,
    family TEXT,
    fam_priv TEXT,
    hosp TEXT,
    flag TEXT,
    service TEXT,
    address1 TEXT,
    address2 TEXT,
    city TEXT,
    province TEXT,
    postal_code TEXT,
    home_phone_number TEXT,
    work_phone_number TEXT,
    ohip TEXT,
    attending TEXT,
    collab1 TEXT,
    collab2 TEXT,
    aurora_file TEXT
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

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    hashed_password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS recovery_tokens (
    email TEXT PRIMARY KEY,
    token TEXT NOT NULL,
    ttl BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS diagnostic_codes (
    diagnostic_code VARCHAR(15) PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    description TEXT NOT NULL
);

-- ACCOUNTS TABLE

CREATE TABLE IF NOT EXISTS accounts (
    account_id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    notes TEXT
);

INSERT INTO diagnostic_codes VALUES
('DX001', 'Hypertension', 'High blood pressure condition'),
('DX002', 'Diabetes', 'Blood glucose regulation issue');

INSERT INTO accounts (patient_id, start_date, end_date, notes)
VALUES
(1, '2024-01-01 08:00:00', NULL, 'Initial admission'),
(1, '2024-03-01 09:30:00', '2024-03-10 12:00:00', 'Follow-up appointment');


