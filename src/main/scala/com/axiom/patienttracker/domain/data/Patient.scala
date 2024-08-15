package com.axiom.patienttracker.domain.data

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class Patient(
    id: Long,
    unitNumber: String,
    lastName: String,
    firstName: String,
    sex: String,
    dob: LocalDate,
    hcn: Option[String] =None,
    family: Option[String] =None,
    famPriv: Option[String] =None,
    hosp: Option[String] =None,
    flag: Option[String] =None,
    address1: Option[String] =None,
    address2: Option[String] =None,
    city: Option[String] =None,
    province: Option[String] =None,
    postalCode: Option[String] =None,
    homePhoneNumber: Option[String] =None,
    workPhoneNumber: Option[String] =None,
    OHIP: Option[String] =None,
    familyPhysician: Option[String] =None,
    attending: Option[String] =None,
    collab1: Option[String] =None,
    collab2: Option[String] =None
)

object Patient:
    given codec: JsonCodec[Patient] = DeriveJsonCodec.gen[Patient]