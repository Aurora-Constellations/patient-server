package com.axiom.patienttracker.domain.data

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class Patient(
    id: Long,
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
)

object Patient:
    given codec: JsonCodec[Patient] = DeriveJsonCodec.gen[Patient]