package com.axiom.patienttracker.http.controllers

import com.axiom.patienttracker.services.ReportService
import sttp.tapir.server.ServerEndpoint
import zio.*
import com.axiom.patienttracker.http.endpoints.ReportEndpoints
import io.getquill.ast.StringOperator.toLong

class ReportController private (service: ReportService) extends BaseController with ReportEndpoints:
    val report: ServerEndpoint[Any, Task] = reportEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("Hey reports!"))

    val create: ServerEndpoint[Any, Task] = createEndpoint
        .serverLogicSuccess(req =>
            service.create(req))

    val getAll: ServerEndpoint[Any, Task] = getAllEndpoint
        .serverLogicSuccess(req =>
            service.getAll())

    val getById: ServerEndpoint[Any, Task] = getByIdEndpoint
        .serverLogicSuccess( id =>
            service.getById(id)
        )
    
    val getByUnitNumber: ServerEndpoint[Any, Task] = getByUnitNumberEndpoint
        .serverLogicSuccess( unitNumber => 
            service.getByUnitNumber(unitNumber))
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById, getByUnitNumber)

object ReportController:
    val makeZIO = ZIO.service[ReportService].map(service => new ReportController(service))