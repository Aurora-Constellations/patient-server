package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.domain.data.Patient

trait PatientEndpoints {
  val patientEndpoint = endpoint
    .tag("patient")
    .name("patient")
    .description("patient tracker")
    .get
    .in("patient")
    .out(plainBody[String])

  val createEndpoint = endpoint
    .tag("patients")
    .name("create")
    .description("create a new patient")
    .in("patients") //path
    .post
    .in(jsonBody[CreatePatientRequest])
    .out(jsonBody[Patient])

  val getAllEndpoint = endpoint
    .tag("patients")
    .name("getAll")
    .description("get all patient data")
    .in("patients")
    .get
    .out(jsonBody[List[Patient]])

  val getByIdEndpoint = endpoint
    .tag("patients")
    .name("getById")
    .description("get patient by id (or maybe by unitNumber?)") //TODO
    .in("patients" / path[String]("id"))
    .get
    .out(jsonBody[Option[Patient]])
}
