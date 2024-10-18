package com.axiom.patienttracker.domain.data

import java.time.LocalDate
import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import java.time.LocalDateTime

final case class Patient(
    id: Long,
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
)

object Patient:
    given codec: JsonCodec[Patient] = DeriveJsonCodec.gen[Patient]