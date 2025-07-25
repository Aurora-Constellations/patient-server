package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package

trait AccountEndpoints extends BaseEndpoint:
    val accountEndpoint = baseEndpoint
        .tag("account")
        .name("account")
        .description("Account Data")
        .get
        .in("account")
        .out(plainBody[String])