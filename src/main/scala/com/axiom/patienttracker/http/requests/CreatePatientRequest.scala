package com.axiom.patienttracker.http.requests

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Patient

final case class CreatePatientRequest(
    unitNumber: String,
    lastName: String,
    firstName: String,
    sex: String,
    dob: LocalDate,
    hcn: Option[String] = None,
    family: Option[String] = None,
    famPriv: Option[String] = None,
    hosp: Option[String] = None,
    flag: Option[String] = None,
    address1: Option[String] = None,
    address2: Option[String] = None,
    city: Option[String] = None,
    province: Option[String] = None,
    postalCode: Option[String] = None,
    homePhoneNumber: Option[String] = None,
    workPhoneNumber: Option[String] = None,
    ohip: Option[String] = None,
    familyPhysician: Option[String] = None,
    attending: Option[String] = None,
    collab1: Option[String] = None,
    collab2: Option[String] = None
):
    def toPatient(id: Long) =
        Patient(id, unitNumber, lastName, firstName, sex, dob, hcn, family, famPriv, hosp, flag, address1, address2, city, province, postalCode, homePhoneNumber, workPhoneNumber, ohip, familyPhysician, attending, collab1, collab2)

object CreatePatientRequest:
    given codec: JsonCodec[CreatePatientRequest] = DeriveJsonCodec.gen[CreatePatientRequest]