package com.axiom.patienttracker.http.requests

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Patient

final case class UpdatePatientRequest(
    account: Option[String] = None,
    unitNumber: Option[String] = None,
    patient: Option[String] = None,
    sex: Option[String] = None,
    dob: Option[LocalDate] = None,
    hcn: Option[String] = None,
    admitDate: Option[LocalDate] = None,
    location: Option[String] = None,
    room: Option[String] = None,
    bed: Option[String] = None,
    admitting: Option[String] = None,
    attending: Option[String] = None,
    family: Option[String] = None,
    famPriv: Option[String] = None,
    hosp: Option[String] = None,
    flag: Option[String] = None,
    service: Option[String] = None
)

object UpdatePatientRequest:
    given codec: JsonCodec[UpdatePatientRequest] = DeriveJsonCodec.gen[UpdatePatientRequest]