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

-- DOCTORS TABLE 

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    provider_id TEXT
);

CREATE INDEX IF NOT EXISTS idx_doctor_provider_id
ON doctors(provider_id);


INSERT INTO patients (
  account_number, unit_number, first_name, last_name, sex, dob, hcn, admit_date,
  floor, room, bed, mrp, admitting_phys, family, fam_priv, hosp, flag, service,
  address1, address2, city, province, postal_code, home_phone_number, work_phone_number,
  ohip, attending, collab1, collab2, aurora_file
) VALUES (
  'ACC123456', 'UNIT7890', 'Alice', 'Johnson', 'F', '1985-04-12', 'HCN123456789', '2025-07-20 14:30:00',
  'Cardiology', '204', 'B', 'Dr. Smith', 'Dr. Adams', 'Yes', 'No', 'General Hospital', 'Fall Risk', 'Cardiac',
  '123 Maple St', 'Apt 5B', 'Toronto', 'ON', 'M4B1B3', '4161234567', '4167654321',
  'OHIP123456', 'Dr. Wilson', 'Dr. Green', 'Dr. Patel', 'AUR123456'
);
INSERT INTO patients (
  account_number, unit_number, first_name, last_name, sex, dob, hcn, admit_date,
  floor, room, bed, mrp, admitting_phys, family, fam_priv, hosp, flag, service,
  address1, address2, city, province, postal_code, home_phone_number, work_phone_number,
  ohip, attending, collab1, collab2, aurora_file
) VALUES (
  'ACC654321', 'UNIT5678', 'Bob', 'Lee', 'M', NULL, 'HCN987654321', '2025-07-19 09:15:00',
  'Neurology', '302', 'A', 'Dr. Carter', 'Dr. Evans', 'No', 'Yes', 'St. Mary''s', NULL, 'Neuro',
  '456 Oak Ave', NULL, 'Ottawa', 'ON', 'K2P2N2', '6135550199', NULL,
  'OHIP654321', 'Dr. Grey', 'Dr. Yang', NULL, 'AUR654321'
);
INSERT INTO patients (
  account_number, unit_number, first_name, last_name, sex, dob, hcn, admit_date,
  floor, room, bed, mrp, admitting_phys, family, fam_priv, hosp, flag, service,
  address1, address2, city, province, postal_code, home_phone_number, work_phone_number,
  ohip, attending, collab1, collab2, aurora_file
) VALUES (
  'ACC777888', 'UNIT9999', 'Charlie', 'Nguyen', 'X', NULL, NULL, '2025-07-18 18:45:00',
  'Psychiatry', NULL, NULL, 'Dr. House', 'Dr. Foreman', 'Yes', NULL, 'City Hospital', NULL, 'Mental Health',
  '789 Birch Rd', NULL, 'Hamilton', 'ON', 'L8P1A1', '9051112233', NULL,
  'OHIP777888', 'Dr. Cameron', NULL, NULL, NULL
);


INSERT INTO diagnostic_codes VALUES
('DX001', 'Hypertension', 'High blood pressure condition'),
('DX002', 'Diabetes', 'Blood glucose regulation issue');

INSERT INTO accounts (patient_id, start_date, end_date, notes)
VALUES
(1, '2024-01-01 08:00:00', NULL, 'Initial admission'),
(1, '2024-03-01 09:30:00', '2024-03-10 12:00:00', 'Follow-up appointment');

INSERT INTO doctors (name, provider_id)
VALUES
('Dr. Gregory House', 'PRV1001'),
('Dr. Miranda Bailey', 'PRV1005');
