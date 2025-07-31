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