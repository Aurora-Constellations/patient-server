-- Sample data insertion for testing

\c patienttracker_test;

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
),
(
  'ACC654321', 'UNIT5678', 'Bob', 'Lee', 'M', NULL, 'HCN987654321', '2025-07-19 09:15:00',
  'Neurology', '302', 'A', 'Dr. Carter', 'Dr. Evans', 'No', 'Yes', 'St. Mary''s', NULL, 'Neuro',
  '456 Oak Ave', NULL, 'Ottawa', 'ON', 'K2P2N2', '6135550199', NULL,
  'OHIP654321', 'Dr. Grey', 'Dr. Yang', NULL, 'AUR654321'
),
(
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
(2, '2024-03-01 09:30:00', '2024-03-10 08:00:00', 'Follow-up appointment'),
(3, '2024-03-01 09:30:00', '2024-03-10 12:00:00', 'Follow-up appointment');

INSERT INTO doctors (name, provider_id)
VALUES
('Dr. Gregory House', 'PRV1001'),
('Dr. Miranda Bailey', 'PRV1005');


-- Row 1: All fields filled properly
INSERT INTO encounters (account_id, doctor_id, start_date, end_date, aurora_file_content
) VALUES (
    1, 1, '2024-07-01 09:00:00', '2024-07-01 10:00:00', decode('5468697320697320612074657374207064662e', 'hex') -- "This is a test pdf."
),
-- Row 2: No end_date (still ongoing)
(
    1, 1, '2024-07-02 14:30:00', NULL, decode('4d6f72652062696e6172792064617461', 'hex') -- "More binary data"
),
-- Row 3: No aurora_file_content (encounter with no file)
(
    2, 2, '2024-07-03 11:15:00', '2024-07-03 11:45:00', NULL
),
-- Row 4: Both end_date and aurora_file_content are NULL
(
    2, 1, '2024-07-04 08:00:00', NULL, NULL
);
-- Row 5: Invalid data for testing CHECK constraint (end_date before start_date)
-- This should fail if CHECK (end_date IS NULL OR end_date >= start_date) is enforced
-- INSERT INTO encounters (account_id, doctor_id, start_date, end_date, aurora_file_content
-- ) VALUES (
--     1, 2, '2024-07-05 12:00:00', '2024-07-05 11:00:00', decode('74657374206261642064617461', 'hex') -- "test bad data"
-- );

-- INVALID DATA TO CHECK DATE CONSTRAINT IN ACCOUNTS TABLE 
--INSERT INTO accounts (patient_id, start_date, end_date)
--VALUES (1, '2024-01-15', '2024-01-01');   

INSERT INTO billing_codes (billing_code, label, amount, description)
VALUES 
('PROC1001', 'General Checkup', 50, 'Routine health check visit'),
('PROC1002', 'Chest X-Ray', 120.755, 'X-ray imaging of chest cavity'),
('PROC2001', 'MRI Brain', 500.654, 'MRI scan to assess brain activity'),
('PROC9001', 'Ultrasound Abdomen', 350.57, 'Abdominal ultrasound exam');

-- Encounter 101: Basic Checkup, with notes
INSERT INTO billings (encounter_id, billing_code, diagnostic_code, recorded_time, unit_count, notes)
VALUES (
    1, 'PROC1001', 'DX001', '2024-01-15 10:30:00', 1, 'Routine yearly checkup.'
),
-- Encounter 101: Chest X-Ray, no notes (edge case: NULL notes)
(
    2, 'PROC1002', 'DX002', '2024-01-15 11:00:00', 1, NULL
),
-- Encounter 102: MRI with multiple units (edge case: high unit count)
(
    3, 'PROC2001', 'DX002', '2024-06-01 14:45:00', 2, 'Patient required repeat scans due to motion artifacts.'
),
-- Encounter 103: Ultrasound, empty notes (edge case: empty string)
(
    4, 'PROC9001', 'DX001', NOW(), 2, ''
),
-- Encounter 103: Duplicate procedure code for same encounter (edge case: same procedure multiple times)
(
    3, 'PROC9001', 'DX001', NOW(), 1, 'Follow-up ultrasound.'
);
