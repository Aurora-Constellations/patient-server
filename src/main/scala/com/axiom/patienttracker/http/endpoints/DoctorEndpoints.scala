package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateDoctorRequest
import com.axiom.patienttracker.domain.data.Doctor
import com.axiom.patienttracker.http.requests.UpdatePatientRequest
import com.axiom.patienttracker.http.requests.UpdateDoctorRequest

trait DoctorEndpoints extends BaseEndpoint:
    val doctorEndpoint = baseEndpoint
        .tag("doctor")
        .name("doctor")
        .description("Doctor Data")
        .get
        .in("doctor")
        .out(plainBody[String])

    val createDoctorEndpoint = baseEndpoint
        .tag("doctor")
        .name("createDoctor")
        .description("Create a new Doctor")
        .post
        .in("doctor")
        .in(jsonBody[CreateDoctorRequest])
        .out(jsonBody[Doctor])

    val getDoctorByProviderIdEndpoint = baseEndpoint
        .tag("doctor")
        .name("getDoctor")
        .description("Get a doctor by provider ID")
        .get
        .in("doctor" / path[String]("providerId"))
        .out(jsonBody[Option[Doctor]])

    val getAllDoctorsEndpoint = baseEndpoint
        .tag("doctor")
        .name("getAllDoctors")
        .description("Get all doctors")
        .get
        .in("doctors")
        .out(jsonBody[List[Doctor]])

    val updateDoctorEndpoint = baseEndpoint
        .tag("doctors")
        .name("update")
        .description("update the doctor details")
        .in("doctor" / "update" / path[String]("providerId"))
        .put
        .in(jsonBody[UpdateDoctorRequest])
        .out(jsonBody[Doctor])

    val deleteDoctorEndpoint = baseEndpoint
        .tag("doctor")
        .name("delete")
        .description("delete the doctor record")
        .delete
        .in("doctor" / "delete" / path[String]("providerId"))
        .out(jsonBody[Doctor])