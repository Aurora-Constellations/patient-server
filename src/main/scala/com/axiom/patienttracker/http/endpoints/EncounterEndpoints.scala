package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateEncounterRequest
import com.axiom.patienttracker.domain.data.Encounter

trait EncounterEndpoints extends BaseEndpoint:
    val encounterEndpoint = baseEndpoint
        .tag("encounter")
        .name("encounter")
        .description("Encounter Health check")
        .get
        .in("encounter")
        .out(jsonBody[String])

    val createEncounterEndpoint = baseEndpoint
        .tag("encounter")
        .name("createEncounter")
        .description("Create a new encounter")
        .post
        .in("encounter")
        .in(jsonBody[CreateEncounterRequest])
        .out(jsonBody[Encounter])

    val getEncounterByIdEndpoint = baseEndpoint
        .tag("encounter")
        .name("getEncounterById")
        .description("Get an encounter by ID")
        .get
        .in("encounter" / path[Long]("encounterId"))
        .out(jsonBody[Option[Encounter]])

    val getAllEncountersEndpoint = baseEndpoint
        .tag("encounter")
        .name("getAllEncounters")
        .description("Get all encounters")
        .get
        .in("encounters")
        .out(jsonBody[List[Encounter]])

    val getEncounterByAccountIdEndpoint = baseEndpoint
        .tag("encounter")
        .name("getEncounterByAccountId")
        .description("Get encounters by account ID")
        .get
        .in("encounters" / "account" / path[Long]("accountId"))
        .out(jsonBody[List[Encounter]])

    val getEncounterByDoctorIdEndpoint = baseEndpoint
        .tag("encounter")
        .name("getEncounterByDoctorId")
        .description("Get encounters by doctor ID")
        .get
        .in("encounters" / "doctor" / path[Long]("doctorId"))
        .out(jsonBody[List[Encounter]])

    val getByAccountAndDoctorIdEndpoint = baseEndpoint
        .tag("encounter")
        .name("getByAccountAndDoctorId")
        .description("Get encounters by Account ID and Doctor ID")
        .get
        .in("encounters" / path[Long]("accountId") / path[Long]("doctorId"))
        .out(jsonBody[List[Encounter]])