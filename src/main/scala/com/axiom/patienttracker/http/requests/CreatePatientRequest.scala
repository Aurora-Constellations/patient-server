package com.axiom.patienttracker.http.requests

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Patient
import java.time.LocalDateTime

final case class CreatePatientRequest(
    accountNumber: String,
    unitNumber: String,
    firstName: String,
    lastName: String,
    sex: String,
    dob: Option[LocalDate] = None,
    hcn: Option[String] = None,
    admitDate: Option[LocalDateTime]= None,
    floor: Option[String] = None,
    room: Option[String] = None,
    bed: Option[String] = None,
    mrp: Option[String] = None,
    admittingPhys: Option[String] = None,
    family: Option[String] = None,
    famPriv: Option[String] = None,
    hosp: Option[String] = None,
    flag: Option[String] = None,
    service: Option[String] = None,
    address1: Option[String] = None,
    address2: Option[String] = None,
    city: Option[String] = None,
    province: Option[String] = None,
    postalCode: Option[String] = None,
    homePhoneNumber: Option[String] = None,
    workPhoneNumber: Option[String] = None,
    ohip: Option[String] = None,
    attending: Option[String] = None,
    collab1: Option[String] = None,
    collab2: Option[String] = None
):
    def toPatient(id: Long) =
        Patient(id, accountNumber, unitNumber, firstName, lastName, sex, dob, hcn, admitDate, floor, room, bed, mrp, admittingPhys, family, famPriv, hosp, flag, service, address1, address2, city, province, postalCode, homePhoneNumber, workPhoneNumber, ohip, attending, collab1, collab2)

object CreatePatientRequest:
    given codec: JsonCodec[CreatePatientRequest] = DeriveJsonCodec.gen[CreatePatientRequest]