package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.UpdateDiagnosticCodeRequest
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
        .in("diagnostic"/ "create")
        .in(jsonBody[DiagnosticCodes])
        .out(jsonBody[DiagnosticCodes])

    val updateDiagnosticCodeEndpoint = baseEndpoint
    .tag("diagnosticCodes")
    .name("updateDiagnosticCode")
    .description("Update an existing Diagnostic Code")
    .put
    .in("diagnostic" / "update" / path[String]("diagnosticCode"))
    .in(jsonBody[UpdateDiagnosticCodeRequest])
    .out(jsonBody[DiagnosticCodes])

    val deleteDiagnosticCodeEndpoint = baseEndpoint
    .tag("diagnosticCodes")
    .name("deleteDiagnosticCode")
    .description("Delete a diagnostic code by code")
    .delete
    .in("diagnostic" /"delete" / path[String]("diagnosticCode"))
    .out(jsonBody[DiagnosticCodes])
