package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateDoctorRequest
import com.axiom.patienttracker.domain.data.Doctor

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