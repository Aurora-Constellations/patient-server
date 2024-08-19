CREATE DATABASE patienttracker;
\c patienttracker;

CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    unitNumber TEXT NOT NULL,
    lastName TEXT NOT NULL,
    firstName TEXT NOT NULL,
    sex TEXT NOT NULL,
    dob DATE NOT NULL,
    hcn TEXT,
    family TEXT,
    famPriv TEXT,
    hosp TEXT,
    flag TEXT,
    address1 TEXT,
    address2 TEXT,
    city TEXT,
    province TEXT,
    postalCode TEXT,
    homePhoneNumber TEXT,
    workPhoneNumber TEXT,
    OHIP TEXT,
    familyPhysician TEXT,
    attending TEXT,
    collab1 TEXT,
    collab2 TEXT
);