package com.axiom.patienttracker.http.controllers

import com.axiom.patienttracker.services.ReportService
import sttp.tapir.server.ServerEndpoint
import zio.*
import com.axiom.patienttracker.http.endpoints.ReportEndpoints

class ReportController private (serice: ReportService) extends BaseController with ReportEndpoints:
    val report: ServerEndpoint[Any, Task] = reportEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("Hey reports!"))
    override val routes: List[ServerEndpoint[Any, Task]] = List(report)

object ReportController:
    val makeZIO = ZIO.service[ReportService].map(service => new ReportController(service))