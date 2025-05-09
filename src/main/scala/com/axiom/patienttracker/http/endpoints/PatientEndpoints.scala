package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.domain.data.Patient
import com.axiom.patienttracker.http.requests.UpdatePatientRequest

trait PatientEndpoints extends BaseEndpoint:
  val patientEndpoint = baseEndpoint
    .tag("patient")
    .name("patient")
    .description("patient tracker")
    .get
    .in("patient")
    .out(plainBody[String])

  val createEndpoint = baseEndpoint
    .tag("patients")
    .name("create")
    .description("create a new patient")
    .in("patients") //path
    .post
    .in(jsonBody[CreatePatientRequest])
    .out(jsonBody[Patient])

  val updateEndpoint = baseEndpoint
    .tag("patients")
    .name("update")
    .description("update the patient details")
    .in("patients" / "update" / path[String]("unitNumber"))
    .put
    .in(jsonBody[UpdatePatientRequest])
    .out(jsonBody[Patient])

  val getAllEndpoint = baseEndpoint
    .tag("patients")
    .name("getAll")
    .description("get all patient data")
    .in("patients")
    .get
    .out(jsonBody[List[Patient]])

  val getByIdEndpoint = baseEndpoint
    .tag("patients")
    .name("getById")
    .description("get patient by id (or maybe by unitNumber?)") //getting by id if converts to long or else get by unit number
    .in("patients" / path[String]("id"))
    .get
    .out(jsonBody[Option[Patient]])

  val deleteEndpoint = baseEndpoint
    .tag("patients")
    .name("delete")
    .description("delete the patient record")
    .delete
    .in("patients" / "delete" / path[Long]("id"))
    .out(jsonBody[Patient])

  val errorEndpoint = baseEndpoint
    .tag("patients")
    .name("error")
    .description("Patient should fail")
    .get
    .in("patients" / "error")
    .out(plainBody[String])