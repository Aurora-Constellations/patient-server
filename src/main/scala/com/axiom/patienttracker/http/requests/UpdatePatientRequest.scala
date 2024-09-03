package com.axiom.patienttracker.http.requests

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Patient

final case class UpdatePatientRequest(
    unitNumber: Option[String] = None,
    lastName: Option[String] = None,
    firstName: Option[String] = None,
    sex: Option[String] = None,
    dob: Option[LocalDate] = None,
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
)

object UpdatePatientRequest:
    given codec: JsonCodec[UpdatePatientRequest] = DeriveJsonCodec.gen[UpdatePatientRequest]