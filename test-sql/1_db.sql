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
    patient_id BIGINT REFERENCES patients(id) ON DELETE CASCADE ON UPDATE CASCADE,
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
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE ON UPDATE CASCADE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    notes TEXT
    CHECK (end_date IS NULL OR start_date < end_date)
);

-- DOCTORS TABLE 

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    provider_id TEXT
);

CREATE INDEX IF NOT EXISTS idx_doctor_provider_id
    ON doctors(provider_id);

-- ENCOUNTERS TABLE

CREATE TABLE IF NOT EXISTS encounters (
    encounter_id      BIGSERIAL PRIMARY KEY,
    account_id        BIGINT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE ON UPDATE CASCADE,
    doctor_id         BIGINT NOT NULL REFERENCES doctors(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    start_date        TIMESTAMP NOT NULL,
    end_date          TIMESTAMP,
    aurora_file_content BYTEA,

    CHECK (end_date IS NULL OR end_date >= start_date)
);

-- Create an index on start_date for faster querying/filtering
CREATE INDEX IF NOT EXISTS idx_encounters_start_date
    ON encounters(start_date);

-- BILLING CODES TABLE 
CREATE TABLE IF NOT EXISTS billing_codes (
    billing_code VARCHAR(15) PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    description TEXT
);

-- BILLINGS TABLE
CREATE TABLE IF NOT EXISTS billings (
    billing_id BIGSERIAL PRIMARY KEY,
    encounter_id BIGINT NOT NULL REFERENCES encounters(encounter_id) ON DELETE CASCADE ON UPDATE CASCADE,
    billing_code VARCHAR(15) NOT NULL REFERENCES billing_codes(billing_code) ON DELETE RESTRICT ON UPDATE CASCADE,
    diagnostic_code VARCHAR(15) NOT NULL REFERENCES diagnostic_codes(diagnostic_code) ON DELETE RESTRICT ON UPDATE CASCADE,
    recorded_time TIMESTAMP NOT NULL,
    unit_count INTEGER NOT NULL,
    notes TEXT
);

-- Indexes for faster lookups on billing_code and diagnostic_code
CREATE INDEX IF NOT EXISTS idx_billing_code 
    ON billings(billing_code);

CREATE INDEX IF NOT EXISTS idx_diagnostic_code 
    ON billings(diagnostic_code);
