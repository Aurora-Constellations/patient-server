package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateAccountRequest
import com.axiom.patienttracker.domain.data.Account

trait AccountEndpoints extends BaseEndpoint:
    val accountEndpoint = baseEndpoint
        .tag("account")
        .name("account")
        .description("Account Data")
        .get
        .in("account")
        .out(plainBody[String])

    val createAccountEndpoint = baseEndpoint
        .tag("account")
        .name("createAccount")
        .description("Create a new account for the patient")
        .post
        .in("account")
        .in(jsonBody[CreateAccountRequest])
        .out(jsonBody[Account])