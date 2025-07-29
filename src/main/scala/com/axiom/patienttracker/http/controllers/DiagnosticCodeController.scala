package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.http.endpoints.DiagnosticCodeEndpoints
import com.axiom.patienttracker.services.DiagnosticCodesService



class DiagnosticCodeController private (service: DiagnosticCodesService)
    extends BaseController
    with DiagnosticCodeEndpoints {

  val diagnostic: ServerEndpoint[Any, Task] = diagnosticCodeEndpoints
    .serverLogicSuccess[Task](_ => ZIO.succeed("DiagnosticCodes module is up"))
  
  val create: ServerEndpoint[Any, Task] =
  createDiagnosticCodeEndpoint.serverLogic(req => service.create(req).either)

  override val routes: List[ServerEndpoint[Any, Task]] = List(diagnostic, create)
}

object DiagnosticCodeController:
    val makeZIO = for{
        service <- ZIO.service[DiagnosticCodesService]
    }yield new DiagnosticCodeController(service)