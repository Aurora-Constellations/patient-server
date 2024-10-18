package com.axiom.patienttracker.http.requests

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Patient

final case class CreatePatientRequest(
    account: String,
    unitNumber: String,
    patient: String,
    sex: String,
    dob: LocalDate,
    hcn: Option[String] = None,
    admitDate: LocalDate,
    location: String,
    room: String,
    bed: String,
    admitting: Option[String] = None,
    attending: Option[String] = None,
    family: Option[String] = None,
    famPriv: Option[String] = None,
    hosp: Option[String] = None,
    flag: Option[String] = None,
    service: Option[String] = None
):
    def toPatient(id: Long) =
        Patient(id, account, unitNumber, patient, sex, dob, hcn, admitDate, location, room, bed, admitting, attending, family, famPriv, hosp, flag, service)

object CreatePatientRequest:
    given codec: JsonCodec[CreatePatientRequest] = DeriveJsonCodec.gen[CreatePatientRequest]