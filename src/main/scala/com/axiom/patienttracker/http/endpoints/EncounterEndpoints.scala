package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package

trait EncounterEndpoints extends BaseEndpoint:
    val encounterEndpoint = baseEndpoint
        .tag("encounter")
        .name("encounter")
        .description("Encounter Health check")
        .get
        .in("encounter")
        .out(jsonBody[String])