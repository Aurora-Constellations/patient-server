package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateDiagnosticCodeRequest
import com.axiom.patienttracker.domain.data.DiagnosticCodes

trait DiagnosticCodeEndpoints extends BaseEndpoint:
    val diagnosticCodeEndpoints = baseEndpoint
        .tag("diagnosticCodes")
        .name("diagnosticCodes")
        .description("Diagnostic Codes")
        .get
        .in("diagnostic")
        .out(plainBody[String])
   
    val createDiagnosticCodeEndpoint = baseEndpoint
        .tag("diagnosticCodes")
        .name("createDiagnosticCode")
        .description("Create a new Diagnostic Code")
        .post
        .in("diagnostic")
        .in(jsonBody[CreateDiagnosticCodeRequest])
        .out(jsonBody[DiagnosticCodes])